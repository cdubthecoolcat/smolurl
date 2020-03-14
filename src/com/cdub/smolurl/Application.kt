package com.cdub.smolurl

import com.cdub.smolurl.controllers.url
import com.cdub.smolurl.models.UrlTable
import com.cdub.smolurl.services.UrlService
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.routing.Routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.serialization
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  install(ContentNegotiation) {
    serialization(
      contentType = ContentType.Application.Json,
      json = Json(
        DefaultJsonConfiguration.copy(
          prettyPrint = true
        )
      )
    )
  }

  Database.connect(
    url = "jdbc:mysql://localhost:3306/smolurl",
    driver = "com.mysql.jdbc.Driver",
    user = "root",
    password = "root"
  )
  transaction {
    SchemaUtils.create(UrlTable)
  }
  val urlService: UrlService = UrlService()

  install(Routing) {
    url(urlService)
  }

}