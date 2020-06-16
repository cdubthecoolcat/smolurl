package me.cewong.smolurl.server.controllers

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import java.io.File
import java.io.FileNotFoundException
import me.cewong.smolurl.models.UrlModel
import me.cewong.smolurl.server.models.ErrorType
import me.cewong.smolurl.server.services.Error
import me.cewong.smolurl.server.services.Success
import me.cewong.smolurl.server.services.UrlService
import me.cewong.smolurl.server.utils.handleError
import me.cewong.smolurl.server.utils.json

val domainExcludeList = try {
  File("excludeList").useLines { it.toList() }
} catch (ex: FileNotFoundException) {
  emptyList<String>()
}

fun Route.url() {
  route("/api/urls") {
    post {
      val newUrl: UrlModel? = call.receiveOrNull()
      domainExcludeListGuard(newUrl) {
        if (newUrl != null) {
          when (val result = UrlService.create(newUrl)) {
            is Success -> call.respond(result.url)
            is Error -> handleError(result.errorType, json.stringify(UrlModel.serializer(), newUrl))
          }
        } else {
          handleError(ErrorType.INVALID_INPUT)
        }
      }
    }
  }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.domainExcludeListGuard(
  model: UrlModel?,
  block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
  if (domainExcludeList.any { model?.target?.contains(it) == true }) {
    if (model != null) {
      handleError(ErrorType.BLOCKED_DOMAIN, json.stringify(UrlModel.serializer(), model))
    } else {
      handleError(ErrorType.BLOCKED_DOMAIN)
    }
  } else {
    block()
  }
}
