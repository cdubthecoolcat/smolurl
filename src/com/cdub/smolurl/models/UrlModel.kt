package com.cdub.smolurl.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

@Serializable
data class UrlModel(
  val id: Long? = null,
  val target: String,
  val short: String
)

object UrlTable : LongIdTable() {
  val target: Column<String> = varchar("target", 255)
  val short: Column<String> = varchar("short", 255)
}

class Url(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Url>(UrlTable)

  var target by UrlTable.target
  var short by UrlTable.short

  fun toModel(): UrlModel = UrlModel(
    this.id.value,
    this.target,
    this.short
  )
}