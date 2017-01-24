package infrastructures.externals.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.polly.AmazonPollyAsyncClient
import com.amazonaws.services.polly.model._
import com.google.inject.Inject
import commons.pimp.RichFutures._

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
 * Created by tsuruta on 2017/01/20.
 */
class AmazonPolly @Inject() (val credentialsProvider: AWSCredentialsProvider) {

  protected val pollyClient = new AmazonPollyAsyncClient(credentialsProvider) {
    setRegion(Region.getRegion(Regions.US_EAST_1)) // TODO support ap-northeast-1
  }

  private def jaVoiceRequest = new DescribeVoicesRequest() {
    setLanguageCode(LanguageCode.JaJP) // japanese voice
  }

  protected val jaVoice: Voice = pollyClient.describeVoices(jaVoiceRequest)
    .getVoices
    .asScala
    .headOption
    .getOrElse(sys.error("no such polly voices (JaJP)"))

  def getMp3(message: String): Future[SynthesizeSpeechResult] = pollyClient
    .synthesizeSpeechAsync(
      new SynthesizeSpeechRequest() {
        setVoiceId(jaVoice.getId)
        setText(message)
        setOutputFormat(OutputFormat.Mp3)
      }
    ).asScala

}
