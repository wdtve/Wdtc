plugins {
  kotlin("jvm") version "1.9.21" apply false
}

subprojects {
  repositories {
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
  }
  apply {
    plugin("application")
  }
}

val versionNumber = "0.0.1.22"
group = "org.wdt.wdtc"
version = "$versionNumber-kotlin"
