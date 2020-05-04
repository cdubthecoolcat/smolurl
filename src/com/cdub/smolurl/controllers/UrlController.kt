package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.services.UrlService
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import java.io.File
import java.io.FileNotFoundException

fun Route.url(service: UrlService) {
  route("/api/urls") {
    post {
      val u: UrlModel? = call.receiveOrNull()
      domainBlacklistGuard(u) {
        if (u != null) {
          call.respond(service.create(u))
        } else {
          call.respondText { "error" }
        }
      }
    }
  }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.domainBlacklistGuard(
  model: UrlModel?,
  block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
  val domainBlacklist = try {
    File("blacklist").useLines { it.toList() }
  } catch (ex: FileNotFoundException) {
    emptyList<String>()
  }

  if (domainBlacklist.any { model?.target?.contains(it) == true }) {
    call.respondText { "domain blocked" }
  } else {
    block()
  }
}
