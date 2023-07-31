package me.cewong.smolurl.server.models

import me.cewong.smolurl.models.UrlModel
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UrlTable : LongIdTable("urls", "pk_alias") {
  val target: Column<String> = varchar("target", 2000)
  val alias: Column<String> = varchar("alias", 255)
  val createdAt: Column<LocalDateTime> = datetime("created_at")
  val updatedAt: Column<LocalDateTime> = datetime("updated_at")
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
