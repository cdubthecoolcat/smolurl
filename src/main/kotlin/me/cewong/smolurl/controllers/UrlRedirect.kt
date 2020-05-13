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
      alias?.let {
        target = UrlService.findByAlias(it)?.target
      }
      target?.let {
        call.respondRedirect(if (it.startsWith("http://") || it.startsWith("https://")) it else "https://$it")
      } ?: handleError(ErrorType.NOT_FOUND, alias!!)
    }
  }
}
