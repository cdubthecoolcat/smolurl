package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.errors.safeCall
import com.cdub.smolurl.services.UrlService
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.urlRedirect(service: UrlService) {
  route("/{alias}") {
    get {
      safeCall {
        val alias = call.parameters["alias"]
        var target: String? = null
        if (alias != null) {
          target = service.findByAlias(alias)?.target
        }
        if (target != null)
          call.respondRedirect(if (target.startsWith("http://") || target.startsWith("https://")) target else "https://$target")
        else {
          throw NotFoundException()
        }
      }
    }
  }
}
