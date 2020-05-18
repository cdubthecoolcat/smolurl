package me.cewong.smolurl.server.services

import java.security.MessageDigest
import java.time.LocalDateTime
import me.cewong.smolurl.models.UrlModel
import me.cewong.smolurl.server.models.ErrorType
import me.cewong.smolurl.server.models.Url
import me.cewong.smolurl.server.models.UrlTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

sealed class Result

class Success(val url: UrlModel) : Result()
class Error(val errorType: ErrorType) : Result()

object UrlService {
  private val aliasRegex = Regex("^[\\w\\-]+$")
  private val urlRegex = Regex("^((?:(https?|ftp|file)://)?[^./]+(?:\\.[^./]+)+(?:/.*)?)$")

  suspend fun findAll(): List<UrlModel> = newSuspendedTransaction {
    Url.all().map { it.toModel() }
  }

  suspend fun find(id: Long?): UrlModel = newSuspendedTransaction {
    id?.let {
      Url[it].toModel()
    } ?: UrlModel(target = "", alias = "")
  }

  suspend fun findByAlias(alias: String): UrlModel? = newSuspendedTransaction {
    Url.find {
      UrlTable.alias eq alias
    }.firstOrNull()?.toModel()
  }

  suspend fun create(url: UrlModel): Result = newSuspendedTransaction {
    val newAlias = if (url.alias.isNotBlank()) url.alias else hash(url.target).substring(0, 6)
    val existingUrl = findByAlias(newAlias)

    when {
      !urlRegex.matches(url.target) -> Error(ErrorType.INVALID_URL)
      url.alias.isNotBlank() && !aliasRegex.matches(url.alias) -> Error(ErrorType.INVALID_ALIAS)
      url.target.isBlank() -> Error(ErrorType.INVALID_INPUT)

      // if custom alias is supplied and that alias is already associated with a target, handle error unless target is the same
      url.alias.isNotBlank() && existingUrl != null &&
        existingUrl.target != url.target -> Error(ErrorType.DUPLICATE)

      else -> Success(existingUrl ?: Url.new {
        target = url.target
        alias = newAlias
        createdAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
      }.toModel())
    }
  }

  suspend fun update(url: UrlModel): UrlModel = newSuspendedTransaction {
    val u = Url[url.id!!]
    u.alias = url.alias
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
