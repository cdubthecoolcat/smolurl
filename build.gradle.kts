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
  kotlin("jvm") version "1.3.72"
}

task("buildReact", Exec::class) {
  workingDir("$rootDir/web")
  inputs.dir("$rootDir/server/src")
  commandLine("yarn", "build")
}
