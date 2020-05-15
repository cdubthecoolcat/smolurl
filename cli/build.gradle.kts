import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

plugins {
  id("org.jlleitschuh.gradle.ktlint")
  kotlin("jvm")
  kotlin("plugin.serialization")
}

dependencies {
  implementation(project(":model"))
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.2.1")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
  implementation("com.squareup.okhttp3:okhttp:4.6.0")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
