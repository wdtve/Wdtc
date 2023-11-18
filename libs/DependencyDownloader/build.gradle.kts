plugins {
  id("java")
}

group = "org.wdt.utils.dependency"
version = rootProject.version

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.dom4j:dom4j:2.1.4")
}

tasks.test {
  useJUnitPlatform()
}