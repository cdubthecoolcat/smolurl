package me.cewong.smolurl.server.controllers

import io.ktor.server.http.content.staticFiles
import io.ktor.server.routing.Route
import java.io.File

fun Route.index() {
  staticFiles("static", File("web/build/static")) {
    staticFiles("css", File("css"))
    staticFiles("js", File("js"))
    staticFiles("media", File("media"))
  }
  staticFiles("/", File("web/build")) {
    File("favicon.ico")
    default("index.html")
  }
}
