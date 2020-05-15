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
  setDependsOn(listOf(":server:build", "buildReact"))
  workingDir(rootDir)
  commandLine("docker-compose", "up", "--build")
}

task("buildReact", Exec::class) {
  workingDir("$rootDir/web")
  inputs.dir("$rootDir/web/src")
  outputs.dir("$rootDir/web/build")
  commandLine("yarn", "build")
}
