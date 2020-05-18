package me.cewong.smolurl.server.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val json = Json(JsonConfiguration.Stable.copy(unquotedPrint = true))
