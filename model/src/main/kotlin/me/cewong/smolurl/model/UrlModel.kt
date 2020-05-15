package me.cewong.smolurl.model

import kotlinx.serialization.Serializable

@Serializable
data class UrlModel(
  val id: Long? = null,
  val target: String,
  val alias: String,
  val createdAt: String? = null,
  val updatedAt: String? = null
)
