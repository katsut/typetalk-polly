package handlers

import java.util

import com.amazonaws.services.lambda.runtime.{ Context, RequestHandler }
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import domains.externals.typetalk.{ TypeTalkRemark, WebhookResponse }

import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import scala.language.postfixOps

trait LambdaHandler extends RequestHandler[util.Map[String, Object], WebhookResponse] {

  val mapper = new ObjectMapper() {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    registerModule(DefaultScalaModule)
  }

  def handleRequest(input: util.Map[String, Object], context: Context): WebhookResponse = {
    val remark = mapper.convertValue(input.get("body"), classOf[TypeTalkRemark])
    println(s"request: ${remark}")

    Await.result(handle(remark, context), context.getRemainingTimeInMillis millisecond) // await until lambda timeout
  }

  protected def handle(requestParam: TypeTalkRemark, context: Context): Future[WebhookResponse]

}