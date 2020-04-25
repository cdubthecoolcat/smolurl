package com.cdub.smolurl

import com.cdub.smolurl.controllers.index
import com.cdub.smolurl.controllers.url
import com.cdub.smolurl.controllers.urlRedirect
import com.cdub.smolurl.models.UrlTable
import com.cdub.smolurl.services.UrlService
import com.cdub.smolurl.utils.isDev
import com.codahale.metrics.Slf4jReporter
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.metrics.dropwizard.DropwizardMetrics
import io.ktor.routing.Routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
  when {
    isDev -> install(DropwizardMetrics) {
      Slf4jReporter.forRegistry(registry)
        .outputTo(log)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()
        .start(10, TimeUnit.SECONDS)
    }
  }

  install(ContentNegotiation) {
    json(
      json = Json(
        DefaultJsonConfiguration.copy(prettyPrint = true)
      ),
      contentType = ContentType.Application.Json
    )
  }

  Database.connect(
    url = "jdbc:postgresql://localhost:5432/smolurl",
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