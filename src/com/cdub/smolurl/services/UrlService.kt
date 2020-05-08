package com.cdub.smolurl.services

import com.cdub.smolurl.models.Url
import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.models.UrlTable
import com.cdub.smolurl.models.errors.DuplicateShortException
import java.security.MessageDigest
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UrlService {
  suspend fun findAll(): List<UrlModel> = newSuspendedTransaction {
    Url.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): UrlModel = newSuspendedTransaction {
    if (id != null) {
      Url[id].toModel()
    }
    UrlModel(target = "", short = "", createdAt = "", updatedAt = "")
  }

  suspend fun findByShort(short: String): UrlModel? = newSuspendedTransaction {
    Url.find {
      UrlTable.short eq short
    }.firstOrNull()?.toModel()
  }

  suspend fun create(url: UrlModel): UrlModel = newSuspendedTransaction {
    val newShort = if (url.short.isNotBlank()) url.short else hash(url.target).substring(0, 6)
    val existingUrl = findByShort(newShort)

    // if custom short is supplied and that short is already associated with a target, throw exception unless target is the same
    if (url.short.isNotBlank() && existingUrl != null && existingUrl.target != url.target) {
      throw DuplicateShortException()
    }

    existingUrl ?: Url.new {
      target = url.target
      short = newShort
      createdAt = LocalDateTime.now()
      updatedAt = LocalDateTime.now()
    }.toModel()
  }

  suspend fun update(url: UrlModel): UrlModel = newSuspendedTransaction {
    val u = Url[url.id!!]
    u.short = url.short
    u.target = url.target
    u.updatedAt = LocalDateTime.now()
    u.toModel()
  }

  suspend fun delete(id: Long): UrlModel = newSuspendedTransaction {
    val u = Url[id]
    u.delete()
    u.toModel()
  }

  private fun hash(url: String): String {
    val HEX_CHARS = "0123456789abcdef"
    val bytes = MessageDigest.getInstance("MD5").digest(url.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
      val i = it.toInt()
      result.append(HEX_CHARS[i shr 4 and 0x0f])
      result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
  }
}
