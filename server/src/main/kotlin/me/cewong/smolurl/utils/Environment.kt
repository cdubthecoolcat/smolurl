package me.cewong.smolurl.utils

import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
val Application.envKind get() = environment.config.property("ktor.environment").getString()
@KtorExperimentalAPI
val Application.isDev get() = envKind == "dev"
@KtorExperimentalAPI
val Application.isProd get() = envKind != "dev"
