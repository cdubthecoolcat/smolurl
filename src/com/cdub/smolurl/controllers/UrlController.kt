package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.services.UrlService
import io.ktor.application.call
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.url(service: UrlService) {
  route("/api/urls") {
    post {
      val u: UrlModel? = call.receiveOrNull()
      if (u != null && service.findByShort(u.short) != null) {
        call.respond(service.create(u))
      } else {
        call.respondText { "error" }
      }
    }
  }
}