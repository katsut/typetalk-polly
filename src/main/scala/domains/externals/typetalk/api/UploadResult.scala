package domains.externals.typetalk.api

import domains.externals.WSResultSuccess
import play.api.libs.json.Json

/**
 * Created by tsuruta on 2017/01/23.
 */
case class UploadResult(
  contentType: String,
  fileKey: String,
  fileName: String,
  fileSize: Int
) extends WSResultSuccess

object UploadResult {
  val reads = Json.reads[UploadResult]
}
