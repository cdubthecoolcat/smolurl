package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.errors.ErrorModel
import com.cdub.smolurl.models.errors.ErrorType
import com.cdub.smolurl.models.errors.safeCall
import com.cdub.smolurl.services.UrlService
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.urlRedirect(service: UrlService) {
  route("/{short}") {
    get {
      safeCall {
        val short = call.parameters["short"]
        var target: String? = null
        if (short != null) {
          target = service.findByShort(short)?.target
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
