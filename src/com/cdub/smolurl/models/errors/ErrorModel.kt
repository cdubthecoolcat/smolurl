package com.cdub.smolurl.models.errors

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime

enum class ErrorType {
  UNKNOWN,
  NOT_FOUND,
  INVALID_URL,
  BLOCKED_DOMAIN,
  DUPLICATE,
  INVALID_INPUT
}

@Serializable
data class ErrorModel(
  val id: Long? = null,
  val type: ErrorType,
  val timestamp: String? = null
)

object ErrorTable : LongIdTable("errors") {
  val type: Column<String> = varchar("type", 255)
  val timestamp: Column<LocalDateTime> = datetime("timestamp")
}

class Error(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Error>(ErrorTable)

  var type by ErrorTable.type
  var timestamp by ErrorTable.timestamp

  fun toModel(): ErrorModel = ErrorModel(
    this.id.value,
    ErrorType.valueOf(type),
    timestamp.toString()
  )
}

suspend fun PipelineContext<Unit, ApplicationCall>.safeCall(
  block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit
) {
  try {
    block()
  } catch (ex: Exception) {
    val exModel = ex.toErrorModel()
    call.respond(exModel.first, exModel.second)
  }
}

fun Exception.toErrorModel(): Pair<HttpStatusCode, ErrorModel> = when (this) {
  is InvalidUrlException -> Pair(HttpStatusCode.BadRequest, ErrorModel(type = ErrorType.INVALID_URL))
  is DuplicateShortException -> Pair(HttpStatusCode.BadRequest, ErrorModel(type = ErrorType.DUPLICATE))
  is InvalidInputException -> Pair(HttpStatusCode.BadRequest, ErrorModel(type = ErrorType.INVALID_INPUT))
  is NotFoundException -> Pair(HttpStatusCode.NotFound, ErrorModel(type = ErrorType.NOT_FOUND))
  is DomainBlockedException -> Pair(HttpStatusCode.Forbidden, ErrorModel(type = ErrorType.BLOCKED_DOMAIN))
  else -> Pair(HttpStatusCode.InternalServerError, ErrorModel(type = ErrorType.UNKNOWN))
}
