package me.cewong.smolurl.models.errors

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
  UNKNOWN("Unknown Error"),
  NOT_FOUND("URL not found"),
  INVALID_URL("Invalid URL"),
  BLOCKED_DOMAIN("Domain not permitted"),
  DUPLICATE("Entry already exists"),
  INVALID_INPUT("Invalid input");
}

@Serializable
data class ErrorModel(
  val id: Long? = null,
  val type: ErrorType,
  val timestamp: String? = null
) {
  val message: String = type.message
}

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

suspend fun PipelineContext<Unit, ApplicationCall>.handleError(type: ErrorType) {
  val error = ErrorService.create(ErrorModel(type = type))
  call.respond(when (type) {
    ErrorType.INVALID_INPUT -> HttpStatusCode.BadRequest
    ErrorType.DUPLICATE -> HttpStatusCode.BadRequest
    ErrorType.INVALID_URL -> HttpStatusCode.BadRequest
    ErrorType.NOT_FOUND -> HttpStatusCode.NotFound
    ErrorType.BLOCKED_DOMAIN -> HttpStatusCode.Forbidden
    else -> HttpStatusCode.InternalServerError
  }, mapOf("error" to error))
}
