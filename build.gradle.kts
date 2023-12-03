subprojects {
  repositories {
    maven { url = uri("https://jitpack.io") }
    mavenLocal()
    mavenCentral()
  }
  apply {
    plugin("java")
    plugin("application")
  }
}

group = "org.wdt.wdtc"
val versionNumber = "0.0.1.17"
version = "$versionNumber-java"
