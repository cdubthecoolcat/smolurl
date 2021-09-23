package me.cewong.smolurl.server.utils

import io.ktor.application.Application

val Application.envKind get() = environment.config.property("ktor.environment").getString()
val Application.isDev get() = envKind == "dev"
val Application.isProd get() = envKind != "dev"
