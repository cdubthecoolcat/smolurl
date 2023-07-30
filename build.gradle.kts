import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "11.5.0"
  id("com.github.johnrengelman.shadow") version "8.1.1"
  kotlin("jvm") version "1.9.0"
  kotlin("plugin.serialization") version "1.9.0"
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
    jvmTarget = "17"
  }
}
