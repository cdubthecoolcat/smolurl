package me.cewong.smolurl.controllers

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
import me.cewong.smolurl.models.errors.DomainBlockedException
import me.cewong.smolurl.models.errors.InvalidInputException
import me.cewong.smolurl.models.errors.safeCall
import me.cewong.smolurl.services.UrlService

val domainBlacklist = try {
  File("blacklist").useLines { it.toList() }
} catch (ex: FileNotFoundException) {
  emptyList<String>()
}

fun Route.url(service: UrlService) {
  route("/api/urls") {
    post {
      val u: UrlModel? = call.receiveOrNull()
      safeCall {
        domainBlacklistGuard(u) {
          if (u != null) {
            call.respond(service.create(u))
          } else {
            throw InvalidInputException()
          }
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
    throw DomainBlockedException()
  } else {
    block()
  }
}
