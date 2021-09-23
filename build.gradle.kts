import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
    maven("https://kotlin.bintray.com/ktor")
  }
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
  id("com.github.johnrengelman.shadow") version "7.0.0"
  kotlin("jvm") version "1.5.31"
  kotlin("plugin.serialization") version "1.5.31"
}

task("dockerUp", Exec::class) {
  setDependsOn(listOf(":server:build", "buildWeb"))
  workingDir(rootDir)
  commandLine("docker-compose", "up", "--build")
}

task("webDependencies", Exec::class) {
  workingDir("$rootDir/web")
  inputs.file("$workingDir/yarn.lock")
  outputs.dir("$workingDir/node_modules")
  commandLine("yarn", "install", "--frozen-lockfile")
}

task("buildWeb", Exec::class) {
  setDependsOn(listOf("webDependencies"))
  workingDir("$rootDir/web")
  inputs.dir("$workingDir/src")
  outputs.dir("$workingDir/build")
  commandLine("yarn", "build")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
