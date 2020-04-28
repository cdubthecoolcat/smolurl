package com.cdub.smolurl.models

import kotlinx.serialization.Serializable

enum class ErrorType {
  DOES_NOT_EXIST,
  INVALID_URL,
  DUPLICATE
}

@Serializable
data class ErrorModel(
  val type: ErrorType,
  val message: String
)