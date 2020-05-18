package me.cewong.smolurl.controllers

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import me.cewong.smolurl.models.ErrorType
import me.cewong.smolurl.services.UrlService
import me.cewong.smolurl.utils.handleError

val regex = Regex("^(https?|ftp)://.+")
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
