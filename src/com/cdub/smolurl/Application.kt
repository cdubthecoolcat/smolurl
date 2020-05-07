package com.cdub.smolurl

import com.cdub.smolurl.controllers.index
import com.cdub.smolurl.controllers.url
import com.cdub.smolurl.controllers.urlRedirect
import com.cdub.smolurl.models.UrlTable
import com.cdub.smolurl.services.UrlService
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.routing.Routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
  install(ContentNegotiation) {
    json(
      json = Json(
        DefaultJsonConfiguration.copy(prettyPrint = true)
      ),
      contentType = ContentType.Application.Json
    )
  }

  Database.connect(
    url = "jdbc:postgresql://postgres:5432/smolurl",
    driver = "org.postgresql.Driver",
    user = "root",
    password = "root"
  )
  transaction {
    SchemaUtils.create(UrlTable)
  }
  val urlService = UrlService()

  install(Routing) {
    index()
    url(urlService)
    urlRedirect(urlService)
  }
}
