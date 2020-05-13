package me.cewong.smolurl.services

import java.time.LocalDateTime
import me.cewong.smolurl.models.Error
import me.cewong.smolurl.models.ErrorModel
import me.cewong.smolurl.models.ErrorType
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ErrorService {
  suspend fun findAll(): List<ErrorModel> = newSuspendedTransaction {
    Error.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): ErrorModel = newSuspendedTransaction {
    id?.let {
      Error[it].toModel()
    } ?: ErrorModel(type = ErrorType.UNKNOWN, metadata = "")
  }

  suspend fun create(error: ErrorModel): ErrorModel = newSuspendedTransaction {
    Error.new {
      type = error.type.toString()
      metadata = error.metadata
      timestamp = LocalDateTime.now()
    }.toModel()
  }

  suspend fun update(error: ErrorModel): ErrorModel = newSuspendedTransaction {
    val e = Error[error.id!!]
    e.type = error.type.toString()
    e.metadata = error.metadata
    e.timestamp = LocalDateTime.now()
    e.toModel()
  }

  suspend fun delete(id: Long): ErrorModel = newSuspendedTransaction {
    val u = Error[id]
    u.delete()
    u.toModel()
  }
}
