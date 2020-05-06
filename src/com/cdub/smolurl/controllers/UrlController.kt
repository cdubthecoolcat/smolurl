package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.ErrorModel
import com.cdub.smolurl.models.ErrorType
import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.services.UrlService
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import java.io.File
import java.io.FileNotFoundException

val domainBlacklist = try {
  File("blacklist").useLines { it.toList() }
} catch (ex: FileNotFoundException) {
  emptyList<String>()
}

fun Route.url(service: UrlService) {
  route("/api/urls") {
    post {
      val u: UrlModel? = call.receiveOrNull()
      domainBlacklistGuard(u) {
        if (u != null && Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]").matches(u.target)) {
          call.respond(service.create(u))
        } else {
          call.respond(HttpStatusCode.NotAcceptable, ErrorModel(ErrorType.INVALID_URL, "The submitted url is invalid."))
        }
      }
    }
  }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.domainBlacklistGuard(
  model: UrlModel?,
  block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
  if (domainBlacklist.any { model?.target?.contains(it) == true }) {
    call.respondText { "domain blocked" }
  } else {
    block()
  }
}
