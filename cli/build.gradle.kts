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
  mainClassName = "me.cewong.smolurl.cli.CliKt"
}

tasks.withType(ShadowJar::class) {
  archiveBaseName.set("smolurl-cli")
}

dependencies {
  implementation(project(":model"))
  implementation(kotlin("stdlib"))
  implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.2")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")
  implementation("com.squareup.okhttp3:okhttp:4.9.1")
}

tasks.withType(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
