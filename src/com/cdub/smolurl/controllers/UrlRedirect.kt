package com.cdub.smolurl.controllers

import com.cdub.smolurl.services.UrlService
import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.urlRedirect(service: UrlService) {
  route("/{short}") {
    get {
      val short = call.parameters["short"]
      var target: String? = null
      if (short != null) {
        target = service.findByShort(short)?.target
      }
      if (target != null)
        call.respondRedirect(if (target.startsWith("http://") || target.startsWith("https://")) target else "https://$target")
      else {
        call.respondText("error")
      }
    }
  }
}
