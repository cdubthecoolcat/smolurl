package com.cdub.smolurl.models

import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime

@Serializable
data class UrlModel(
  val id: Long? = null,
  val target: String,
  val alias: String,
  val createdAt: String? = null,
  val updatedAt: String? = null
)

object UrlTable : LongIdTable("urls") {
  val target: Column<String> = varchar("target", 255)
  val alias: Column<String> = varchar("alias", 255)
  val createdAt: Column<LocalDateTime> = datetime("created_at")
  val updatedAt: Column<LocalDateTime> = datetime("updated_at")
  override val primaryKey = PrimaryKey(alias, name = "pk_alias")
}

class Url(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Url>(UrlTable)

  var target by UrlTable.target
  var alias by UrlTable.alias
  var createdAt by UrlTable.createdAt
  var updatedAt by UrlTable.updatedAt

  fun toModel(): UrlModel = UrlModel(
    this.id.value,
    this.target,
    this.alias,
    this.createdAt.toString(),
    this.updatedAt.toString()
  )
}
