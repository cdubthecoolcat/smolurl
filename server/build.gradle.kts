import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val postgresqlVersion: String by project

plugins {
  application
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
  id("com.github.johnrengelman.shadow") version "5.2.0"
  kotlin("jvm") version "1.3.72"
  kotlin("plugin.serialization") version "1.3.72"
}

group = "me.cewong.smolurl"
version = "0.0.1"

application {
  mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType(ShadowJar::class) {
  archiveBaseName.set("smolurl")
}

dependencies {
  implementation("ch.qos.logback", "logback-classic", logbackVersion)
  implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", kotlinVersion)
  implementation("io.ktor", "ktor-server-netty", ktorVersion)
  implementation("io.ktor", "ktor-server-core", ktorVersion)
  implementation("io.ktor", "ktor-serialization", ktorVersion)
  implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
  implementation("org.jetbrains.exposed", "exposed-java-time", exposedVersion)
  implementation("org.postgresql", "postgresql", postgresqlVersion)
  testImplementation("io.ktor", "ktor-server-tests", ktorVersion)
}

task("buildReact", Exec::class) {
  workingDir("$workingDir/web")
  inputs.dir("$workingDir/src")
  commandLine("yarn", "build")
}

task("dockerUp", Exec::class) {
  setDependsOn(listOf("build", "buildReact"))
  workingDir(rootDir)
  commandLine("docker-compose", "up", "--build")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")