package infrastructures.externals.typetalk

import java.io.InputStream

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.{ Source, StreamConverters }
import akka.util.ByteString
import com.google.inject.Inject
import domains.externals.typetalk.api.UploadResult
import domains.externals.{ WSResult, WSResultFailure }
import play.api.libs.ws._
import play.api.mvc.MultipartFormData.FilePart

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * TODO 抽象化
 * Created by tsuruta on 2017/01/14.
 */
class Typetalk @Inject() (val ws: WSClient) {

  def uploadFile(topicId: Int, stream: () => InputStream): Future[WSResult] = {
    // Upload API => https://developer.nulab-inc.com/ja/docs/typetalk/api/1/upload-attachment
    val endpoint = s"https://typetalk.in/api/v1/topics/$topicId/attachments"
    println(s"# endpoint: $endpoint")

    val file: Source[FilePart[Source[ByteString, Future[IOResult]]], NotUsed] =
      Source.single(FilePart("file", "voice.mp3", Some("audio/mp3"), StreamConverters.fromInputStream(stream)))

    ws
      .url(endpoint)
      .withHeaders("X-Typetalk-Token" -> sys.env.getOrElse("TYPETALK_TOKEN", sys.error("require environment variable: TYPETALK_TOKEN")))
      .post(file)
      .map(
        _.json
        .validate[UploadResult](UploadResult.reads)
        .recover { case err => new WSResultFailure(err.errors.mkString(", "), err.errors.headOption.map(e => new RuntimeException(e._2.mkString(",")))) }
        .get
      )
  }

}
