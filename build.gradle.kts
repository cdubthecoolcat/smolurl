allprojects {
  repositories {
    mavenLocal()
    maven("https://plugins.gradle.org/m2/")
    jcenter()
    maven("https://kotlin.bintray.com/ktor")
  }
}

task("buildReact", Exec::class) {
  workingDir("$rootDir/web")
  inputs.dir("$rootDir/server/src")
  commandLine("yarn", "build")
}
