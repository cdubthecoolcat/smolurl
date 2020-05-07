package com.cdub.smolurl.services

import com.cdub.smolurl.models.errors.Error
import com.cdub.smolurl.models.errors.ErrorModel
import com.cdub.smolurl.models.errors.ErrorType
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class ErrorService {
  suspend fun findAll(): List<ErrorModel> = newSuspendedTransaction {
    Error.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): ErrorModel = newSuspendedTransaction {
    if (id != null) {
      Error[id].toModel()
    }
    ErrorModel(type = ErrorType.DUPLICATE, timestamp = "")
  }

  suspend fun create(error: ErrorModel): ErrorModel = newSuspendedTransaction {
    Error.new {
      type = error.type.toString()
      timestamp = LocalDateTime.now()
    }.toModel()
  }

  suspend fun update(error: ErrorModel): ErrorModel = newSuspendedTransaction {
    val e = Error[error.id!!]
    e.type = error.type.toString()
    e.timestamp = LocalDateTime.now()
    e.toModel()
  }

  suspend fun delete(id: Long): ErrorModel = newSuspendedTransaction {
    val u = Error[id]
    u.delete()
    u.toModel()
  }
}
