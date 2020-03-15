package com.cdub.smolurl.controllers

import io.ktor.http.content.*
import io.ktor.routing.Route
import java.io.File

fun Route.index() {
  static("static") {
    staticRootFolder = File("frontend/build/static")
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
    staticRootFolder = File("frontend/build")
    file("favicon.ico")
    default("index.html")
  }
}