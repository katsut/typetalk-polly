package domains.externals

/**
 * Created by tsuruta on 2017/01/20.
 */
trait WSResult

trait WSResultSuccess extends WSResult

case class WSResultFailure(message: String, error: Option[Throwable]) extends WSResult
