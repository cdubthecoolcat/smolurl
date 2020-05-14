import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
  kotlin("jvm") version "1.3.72"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.2.1")
  implementation("com.squareup.okhttp3:okhttp:4.6.0")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
