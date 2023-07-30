import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

plugins {
  application
  id("org.jlleitschuh.gradle.ktlint")
  id("com.github.johnrengelman.shadow")
  kotlin("jvm")
  kotlin("plugin.serialization")
}

application {
  mainClass.set("me.cewong.smolurl.cli.CliKt")
}

tasks.withType(ShadowJar::class) {
  archiveBaseName.set("smolurl-cli")
}

dependencies {
  implementation(project(":model"))
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
  implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "17"
  }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
