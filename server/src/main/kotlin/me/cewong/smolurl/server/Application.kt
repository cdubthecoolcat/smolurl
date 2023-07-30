package me.cewong.smolurl.server

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import me.cewong.smolurl.server.controllers.index
import me.cewong.smolurl.server.controllers.url
import me.cewong.smolurl.server.controllers.urlRedirect
import me.cewong.smolurl.server.models.ErrorTable
import me.cewong.smolurl.server.models.UrlTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
  install(ContentNegotiation) {
    json(
      json = Json {
        prettyPrint = true
      },
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
    SchemaUtils.create(UrlTable, ErrorTable)
  }

  routing {
    index()
    url()
    urlRedirect()
  }
}
