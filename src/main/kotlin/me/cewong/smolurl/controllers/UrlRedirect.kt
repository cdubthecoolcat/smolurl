package me.cewong.smolurl.controllers

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import me.cewong.smolurl.models.ErrorType
import me.cewong.smolurl.models.handleError
import me.cewong.smolurl.services.UrlService

fun Route.urlRedirect() {
  route("/{alias}") {
    get {
      val alias = call.parameters["alias"]
      var target: String? = null
      if (alias != null) {
        target = UrlService.findByAlias(alias)?.target
      }
      if (target != null)
        call.respondRedirect(if (target.startsWith("http://") || target.startsWith("https://")) target else "https://$target")
      else {
        handleError(ErrorType.NOT_FOUND)
      }
    }
  }
}
