package me.cewong.smolurl.models

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import me.cewong.smolurl.services.ErrorService
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime

@Serializable
enum class ErrorType(val message: String) {
  BLOCKED_DOMAIN("Domain not permitted"),
  DUPLICATE("Entry already exists"),
  INVALID_INPUT("Invalid input"),
  INVALID_URL("Invalid URL"),
  NOT_FOUND("URL not found"),
  UNKNOWN("Unknown Error")
}

@Serializable
data class ErrorModel(
  val id: Long? = null,
  val type: ErrorType,
  val metadata: String,
  val timestamp: String? = null
) {
  val message: String = type.message
}

object ErrorTable : LongIdTable("errors") {
  val type: Column<String> = varchar("type", 64)
  val metadata: Column<String> = text("metadata")
  val timestamp: Column<LocalDateTime> = datetime("timestamp")
}

class Error(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Error>(ErrorTable)

  var type by ErrorTable.type
  var metadata by ErrorTable.metadata
  var timestamp by ErrorTable.timestamp

  fun toModel(): ErrorModel = ErrorModel(
    this.id.value,
    ErrorType.valueOf(type),
    metadata,
    timestamp.toString()
  )
}

suspend fun PipelineContext<Unit, ApplicationCall>.handleError(type: ErrorType, metadata: String = "") {
  val error = ErrorService.create(ErrorModel(type = type, metadata = metadata))
  call.respond(when (type) {
    ErrorType.BLOCKED_DOMAIN -> HttpStatusCode.Forbidden
    ErrorType.DUPLICATE -> HttpStatusCode.BadRequest
    ErrorType.INVALID_INPUT -> HttpStatusCode.BadRequest
    ErrorType.INVALID_URL -> HttpStatusCode.BadRequest
    ErrorType.NOT_FOUND -> HttpStatusCode.NotFound
    else -> HttpStatusCode.InternalServerError
  }, mapOf("error" to error))
}
