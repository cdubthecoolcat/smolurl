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
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.2.0")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
