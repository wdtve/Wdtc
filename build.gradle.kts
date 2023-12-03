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

val versionNumber = "0.0.1.18"
group = "org.wdt.wdtc"
version = "$versionNumber-kotlin"
