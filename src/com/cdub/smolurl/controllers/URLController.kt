package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.URLModel
import com.cdub.smolurl.models.errors.DomainBlockedException
import com.cdub.smolurl.models.errors.InvalidInputException
import com.cdub.smolurl.models.errors.safeCall
import com.cdub.smolurl.services.URLService
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

val domainBlacklist = try {
  File("blacklist").useLines { it.toList() }
} catch (ex: FileNotFoundException) {
  emptyList<String>()
}

fun Route.url(service: URLService) {
  route("/api/urls") {
    post {
      val u: URLModel? = call.receiveOrNull()
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
  model: URLModel?,
  block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
  if (domainBlacklist.any { model?.target?.contains(it) == true }) {
    throw DomainBlockedException()
  } else {
    block()
  }
}
