package me.cewong.smolurl.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.cewong.smolurl.models.UrlModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

private const val BASE_URL = "https://smolurl.cewong.me"
private val json = Json.Default

fun main(args: Array<String>) {
  val parser = ArgParser("smolurl")
  val target by parser.option(
    ArgType.String,
    shortName = "t",
    fullName = "target",
    description = "The target URL to shorten"
  ).required()
  val alias by parser.option(
    ArgType.String,
    shortName = "a",
    fullName = "alias",
    description = "The custom alias to use"
  )
  parser.parse(args)

  val okHttpClient = OkHttpClient()
  val requestBody = """
    {
        alias: "${alias ?: ""}",
        target: "$target"
    }
  """.toRequestBody("application/json".toMediaType())
  val request = Request.Builder()
    .url("$BASE_URL/api/urls")
    .post(requestBody)
    .build()
  try {
    val response = okHttpClient.newCall(request).execute()
    val responseBody = response.body?.string()

    println(
      if (response.isSuccessful && responseBody != null) {
        val model = json.decodeFromString<UrlModel>(UrlModel.serializer(), responseBody)
        "$BASE_URL/${model.alias}"
      } else {
        "Error shortening url: $target"
      }
    )
  } catch (ex: Exception) {
    println("Error shortening url: $target")
  }
}
