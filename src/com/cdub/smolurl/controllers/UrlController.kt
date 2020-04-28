package com.cdub.smolurl.controllers

import com.cdub.smolurl.models.ErrorModel
import com.cdub.smolurl.models.ErrorType
import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.services.UrlService
import io.ktor.application.call
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import java.sql.BatchUpdateException

fun Route.url(service: UrlService) {
  route("/api/urls") {
    post {
      val u: UrlModel? = call.receiveOrNull()
      if (u != null) {
        try {
          val newUrl: UrlModel = service.create(u)
          call.respond(newUrl)
        } catch (e: ExposedSQLException) {
          call.respond(ErrorModel(ErrorType.DUPLICATE, "The submitted url already exists."))
        }
      } else {
        call.respond(ErrorModel(ErrorType.INVALID_URL, "The submitted url is invalid."))
      }
    }
  }
}