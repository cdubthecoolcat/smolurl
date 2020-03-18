package com.cdub.smolurl.services

import com.cdub.smolurl.models.Url
import com.cdub.smolurl.models.UrlModel
import com.cdub.smolurl.models.UrlTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.security.MessageDigest

class UrlService {
  suspend fun findAll(): List<UrlModel> = newSuspendedTransaction {
    Url.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): UrlModel = newSuspendedTransaction {
    if (id != null) {
      Url[id].toModel()
    }
    UrlModel(target = "", short = "")
  }

  suspend fun findByShort(short: String): UrlModel = newSuspendedTransaction {
    Url.find {
      UrlTable.short eq short
    }.first().toModel()
  }

  suspend fun create(url: UrlModel): UrlModel = newSuspendedTransaction {
    Url.new {
      target = url.target
      short = hash(url.target).substring(0, 6)
    }.toModel()
  }

  suspend fun update(url: UrlModel): UrlModel = newSuspendedTransaction {
    val u = Url[url.id!!]
    u.short = url.short
    u.target = url.target
    u.toModel()
  }

  suspend fun delete(id: Long): UrlModel = newSuspendedTransaction {
    val u = Url[id]
    u.delete()
    u.toModel()
  }

  private fun hash(url: String): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest.getInstance("MD5").digest(url.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach { 
      val i = it.toInt()
      result.append(HEX_CHARS[i shr 4 and 0x0f])
      result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString();
  }
}
