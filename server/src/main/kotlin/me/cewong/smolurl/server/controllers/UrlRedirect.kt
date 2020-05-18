package me.cewong.smolurl.server.controllers

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import me.cewong.smolurl.server.models.ErrorType
import me.cewong.smolurl.server.services.UrlService
import me.cewong.smolurl.server.utils.handleError

val regex = Regex("^(https?|ftp|file)://.+")
fun makeUri(target: String): String = if (regex.matches(target)) {
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
