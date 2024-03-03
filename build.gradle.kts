subprojects {
  repositories {
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
  }
  apply {
    plugin("application")
  }
}

val versionNumber = "0.0.2.1"
group = "org.wdt.wdtc"
version = "$versionNumber-kotlin"
