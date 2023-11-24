plugins {
  id("java")
  kotlin("jvm") version "1.9.20"
}

group = "org.wdt.utils.dependency"
version = rootProject.version

repositories {
  maven { url = uri("https://jitpack.io") }
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("org.dom4j:dom4j:2.1.4")
  implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
  useJUnitPlatform()
}
