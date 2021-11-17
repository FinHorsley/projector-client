plugins {
  kotlin("jvm")
  `maven-publish`
  jacoco
}

setupJacoco()

kotlin {
  explicitApi()
}

publishToSpace("java")

val intellijPlatformVersion: String by project

dependencies {
  api(project(":projector-util-logging"))

  compileOnly("com.jetbrains.intellij.platform:bootstrap:$intellijPlatformVersion")
  compileOnly("com.jetbrains.intellij.platform:util-diagnostic:$intellijPlatformVersion")

  testImplementation(kotlin("test"))
}
