package handlers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.amazonaws.auth.{ AWSCredentialsProvider, EnvironmentVariableCredentialsProvider }
import com.amazonaws.services.lambda.runtime.Context
import com.google.inject.{ AbstractModule, Guice, _ }
import domains.externals.typetalk.api.UploadResult
import domains.externals.typetalk.{ TypeTalkRemark, WebhookFailure, WebhookResponse, WebhookSuccess }
import domains.externals.{ WSResult, WSResultFailure }
import infrastructures.externals.aws.AmazonPolly
import infrastructures.externals.typetalk.Typetalk
import net.codingwell.scalaguice.InjectorExtensions._
import net.codingwell.scalaguice.ScalaModule
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

class UploadVoiceHandler extends LambdaHandler {

  val injector = Guice.createInjector(new AbstractModule with ScalaModule {
    override def configure(): Unit = {
      implicit val system = ActorSystem()
      val ws = AhcWSClient()(ActorMaterializer())
      bind[AWSCredentialsProvider].to[EnvironmentVariableCredentialsProvider].in[Singleton]
      bind[ActorSystem].toInstance(system)
      bind[WSClient].toInstance(ws)
      bind[AmazonPolly].in[Singleton]
      bind[Typetalk].in[Singleton]
      // hook: destroy lambda container
      sys.addShutdownHook {
        system.terminate
        ws.close
      }
    }
  })

  val (polly, typetalk) = (injector.instance[AmazonPolly], injector.instance[Typetalk])

  /**
   * [Amazon polly連携] webhookで取得したメッセージを音声データに変換し、TypeTalkにアップロード
   *
   * @param remark
   * @param context
   * @return
   */
  override def handle(remark: TypeTalkRemark, context: Context): Future[WebhookResponse] = for {
    voiceStream <- polly.getMp3(generateReadMessage(remark)) // get voice stream
    uploadResponse <- typetalk.uploadFile(remark.topic.id, voiceStream.getAudioStream) // upload file
  } yield toResponse(uploadResponse)

  private def toResponse(wsResponse: WSResult): WebhookResponse = wsResponse match {
    case r: UploadResult    => WebhookSuccess("TODO: create Play mp3 client ::: html5 or electron", None, r.fileKey :: Nil)
    case e: WSResultFailure => WebhookFailure(s"bot failed: ${e.message}", None)
  }

  private def generateReadMessage(requestParam: TypeTalkRemark) = s"${requestParam.post.account.name}。 ${requestParam.post.message}"

}
