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
      if (short != null) {
        val target = service.findByShort(short).target
        call.respondRedirect(if (target.startsWith("http://")) target else "http://$target")
      } else {
        call.respondText("error")
      }
    }
  }
}
