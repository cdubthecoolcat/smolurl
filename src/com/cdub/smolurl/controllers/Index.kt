package com.cdub.smolurl.controllers

import io.ktor.http.content.default
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.routing.Route
import java.io.File

fun Route.index() {
  static("static") {
    staticRootFolder = File("web/build/static")
    static("css") {
      files("css")
    }
    static("js") {
      files("js")
    }
    static("media") {
      files("media")
    }
  }
  static {
    staticRootFolder = File("web/build")
    file("favicon.ico")
    default("index.html")
  }
}
