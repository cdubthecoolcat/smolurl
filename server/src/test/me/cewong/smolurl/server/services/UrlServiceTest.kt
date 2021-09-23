package me.cewong.smolurl.server.services

import kotlinx.coroutines.runBlocking
import me.cewong.smolurl.models.UrlModel
import me.cewong.smolurl.server.BaseTest
import me.cewong.smolurl.server.models.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlServiceTest : BaseTest() {

  @Test
  fun `create url with valid url returns success`() {
    runBlocking {
      val urlModel = UrlModel(
        target = "google.com",
        alias = ""
      )
      val result = UrlService.create(urlModel)
      assertTrue(result is Success)
      assertEquals("google.com", (result as Success).url.target)
    }
  }

  @Test
  fun `create url with invalid url returns error`() {
    runBlocking {
      val urlModel = UrlModel(
        target = "invalid_url",
        alias = ""
      )
      val result = UrlService.create(urlModel)
      assertTrue(result is Error)
      assertEquals(ErrorType.INVALID_URL, (result as Error).errorType)
    }
  }

  @Test
  fun `create url with invalid alias returns error`() {
    runBlocking {
      val urlModel = UrlModel(
        target = "google.com",
        alias = "...."
      )
      val result = UrlService.create(urlModel)
      assertTrue(result is Error)
      assertEquals(ErrorType.INVALID_ALIAS, (result as Error).errorType)
    }
  }

  @Test
  fun `create url with valid alias returns success`() {
    runBlocking {
      val urlModel = UrlModel(
        target = "google.com",
        alias = "alias--__"
      )
      val result = UrlService.create(urlModel)
      assertTrue(result is Success)
      assertEquals("google.com", (result as Success).url.target)
      assertEquals("alias--__", result.url.alias)
    }
  }

  @Test
  fun `create url with existing alias and different url returns error`() {
    runBlocking {
      val firstAliasModel = UrlModel(
        target = "google.com",
        alias = "alias"
      )
      UrlService.create(firstAliasModel)

      val duplicateAliasModel = UrlModel(
        target = "facebook.com",
        alias = "alias"
      )
      val result = UrlService.create(duplicateAliasModel)
      assertTrue(result is Error)
      assertEquals(ErrorType.DUPLICATE, (result as Error).errorType)
    }
  }

  @Test
  fun `create url with existing alias but same url returns success`() {
    runBlocking {
      val firstAliasModel = UrlModel(
        target = "google.com",
        alias = "alias"
      )
      UrlService.create(firstAliasModel)

      val duplicateAliasModel = UrlModel(
        target = "google.com",
        alias = "alias"
      )
      val result = UrlService.create(duplicateAliasModel)
      assertTrue(result is Success)
      assertEquals("google.com", (result as Success).url.target)
      assertEquals("alias", result.url.alias)
    }
  }
}
