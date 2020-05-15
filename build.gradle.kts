allprojects {
  repositories {
    mavenLocal()
    maven("https://plugins.gradle.org/m2/")
    jcenter()
    maven("https://kotlin.bintray.com/ktor")
  }
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
  id("com.github.johnrengelman.shadow") version "5.2.0"
  kotlin("jvm") version "1.3.72"
  kotlin("plugin.serialization") version "1.3.72"
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
