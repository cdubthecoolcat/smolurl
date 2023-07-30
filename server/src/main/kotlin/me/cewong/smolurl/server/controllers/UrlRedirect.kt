package me.cewong.smolurl.server.controllers

import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import me.cewong.smolurl.server.models.ErrorType
import me.cewong.smolurl.server.services.UrlService
import me.cewong.smolurl.server.utils.handleError

val uriRegex = Regex("^(https?|ftp|file)://.+")
fun makeUri(target: String): String = if (uriRegex.matches(target)) {
  target
} else {
  "https://$target"
}

fun Route.urlRedirect() {
  route("/{alias}") {
    get {
      val alias = call.parameters["alias"]
      val target = alias?.let {
        UrlService.findByAlias(it)?.target
      }
      if (target != null) {
        call.respondRedirect(makeUri(target))
      } else {
        handleError(ErrorType.NOT_FOUND, alias!!)
      }
    }
  }
}
