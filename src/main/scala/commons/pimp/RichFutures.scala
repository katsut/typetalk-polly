package commons.pimp

import java.util.concurrent.{ Future => JavaFuture }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

/**
 * Created by tsuruta on 2017/01/20.
 */
object RichFutures {
  implicit class AsScala[T](jf: JavaFuture[T]) {
    def asScala: Future[T] = Future {
      jf.get()
    }
  }

}
