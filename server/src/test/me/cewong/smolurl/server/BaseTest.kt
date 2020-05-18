package me.cewong.smolurl.server

import java.lang.Exception
import me.cewong.smolurl.server.models.ErrorTable
import me.cewong.smolurl.server.models.UrlTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before

/**
 * The Exposed framework is not very testable/mockable due to the nature of tables and transactions
 * being handled by singletons. This base test creates a lightweight in-memory database that we can use
 * for testing purposes.
 */
abstract class BaseTest {

  @Before
  fun setUp() {
    try {
      Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver",
        user = "root",
        password = ""
      )
      transaction {
        SchemaUtils.create(UrlTable)
        SchemaUtils.create(ErrorTable)
      }
    } catch (ex: Exception) {
      // JUnit executes tests in parallel so multiple connections might be attempted
    }
  }
}
