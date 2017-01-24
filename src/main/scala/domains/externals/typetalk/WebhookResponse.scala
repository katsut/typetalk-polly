package domains.externals.typetalk

import java.util.{ ArrayList => JavaList }

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
 * Created by tsuruta on 2017/01/14.
 * {
 * "message":  "Hello!",
 * "replyTo": 100,
 * "fileKeys": [ "abcdef", "ghijk" ],
 * "talkIds": [ 200, 300 ]
 * }
 */
sealed abstract class WebhookResponse(
  @BeanProperty val message: String,
  @BeanProperty val replyTo: Integer
)

class WebhookSuccess protected (message: String, replyTo: Integer, @BeanProperty val fileKeys: JavaList[String], @BeanProperty val talkIds: JavaList[Integer]) extends WebhookResponse(message, replyTo)

object WebhookSuccess {
  def apply(_message: String, _replyTo: Option[Int], _fileKeys: Seq[String] = Seq.empty[String], _talkIds: Seq[Int] = Seq.empty[Int]): WebhookSuccess =
    new WebhookSuccess(
      _message,
      _replyTo.map(Integer.valueOf).orNull,
      new JavaList[String](_fileKeys.asJava),
      new JavaList[Integer](_talkIds.map(Integer.valueOf).asJava)
    )
}

class WebhookFailure protected (message: String, replyTo: Integer) extends WebhookResponse(message, replyTo)
object WebhookFailure {
  def apply(_message: String, _replyTo: Option[Int]): WebhookFailure = new WebhookFailure(_message, _replyTo.map(Integer.valueOf).orNull)
}
