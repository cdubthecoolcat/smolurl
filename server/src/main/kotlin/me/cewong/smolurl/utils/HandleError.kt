package me.cewong.smolurl.utils

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import me.cewong.smolurl.models.ErrorModel
import me.cewong.smolurl.models.ErrorType
import me.cewong.smolurl.services.ErrorService

suspend fun PipelineContext<Unit, ApplicationCall>.handleError(type: ErrorType, metadata: String = "") {
  val error = ErrorService.create(ErrorModel(type = type, metadata = metadata))
  call.respond(when (type) {
    ErrorType.BLOCKED_DOMAIN -> HttpStatusCode.Forbidden
    ErrorType.DUPLICATE -> HttpStatusCode.BadRequest
    ErrorType.INVALID_ALIAS -> HttpStatusCode.BadRequest
    ErrorType.INVALID_INPUT -> HttpStatusCode.BadRequest
    ErrorType.INVALID_URL -> HttpStatusCode.BadRequest
    ErrorType.NOT_FOUND -> HttpStatusCode.NotFound
    else -> HttpStatusCode.InternalServerError
  }, mapOf("error" to error))
}
