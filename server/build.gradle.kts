import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val postgresqlVersion: String by project
val h2databaseVersion: String by project

plugins {
  application
  id("org.jlleitschuh.gradle.ktlint")
  id("com.github.johnrengelman.shadow")
  kotlin("jvm")
  kotlin("plugin.serialization")
}

group = "me.cewong.smolurl"
version = "0.0.1"

application {
  mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.withType(ShadowJar::class) {
  archiveBaseName.set("smolurl")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(":model"))
  implementation("ch.qos.logback", "logback-classic", logbackVersion)
  implementation("org.jetbrains.kotlin", "kotlin-stdlib", kotlinVersion)
  implementation("io.ktor", "ktor-server-netty", ktorVersion)
  implementation("io.ktor", "ktor-server-core", ktorVersion)
  implementation("io.ktor", "ktor-serialization", ktorVersion)
  implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-java-time", exposedVersion)
  implementation("org.postgresql", "postgresql", postgresqlVersion)
  testImplementation("io.ktor", "ktor-server-tests", ktorVersion)
  testImplementation("com.h2database", "h2", h2databaseVersion)
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

sourceSets["main"].resources.srcDirs("resources")
