package com.cdub.smolurl.services

import com.cdub.smolurl.models.URL
import com.cdub.smolurl.models.URLModel
import com.cdub.smolurl.models.URLTable
import com.cdub.smolurl.models.errors.DuplicateAliasException
import java.security.MessageDigest
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class URLService {
  suspend fun findAll(): List<URLModel> = newSuspendedTransaction {
    URL.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): URLModel = newSuspendedTransaction {
    if (id != null) {
      URL[id].toModel()
    }
    URLModel(target = "", alias = "", createdAt = "", updatedAt = "")
  }

  suspend fun findByAlias(alias: String): URLModel? = newSuspendedTransaction {
    URL.find {
      URLTable.alias eq alias
    }.firstOrNull()?.toModel()
  }

  suspend fun create(url: URLModel): URLModel = newSuspendedTransaction {
    val newAlias = if (url.alias.isNotBlank()) url.alias else hash(url.target).substring(0, 6)
    val existingURL = findByAlias(newAlias)

    // if custom alias is supplied and that alias is already associated with a target, throw exception unless target is the same
    if (url.alias.isNotBlank() && existingURL != null && existingURL.target != url.target) {
      throw DuplicateAliasException()
    }

    existingURL ?: URL.new {
      target = url.target
      alias = newAlias
      createdAt = LocalDateTime.now()
      updatedAt = LocalDateTime.now()
    }.toModel()
  }

  suspend fun update(url: URLModel): URLModel = newSuspendedTransaction {
    val u = URL[url.id!!]
    u.alias = url.alias
    u.target = url.target
    u.updatedAt = LocalDateTime.now()
    u.toModel()
  }

  suspend fun delete(id: Long): URLModel = newSuspendedTransaction {
    val u = URL[id]
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
