subprojects {
  repositories {
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
  }
  apply {
    plugin("application")
  }
}
plugins {
	alias(libs.plugins.kotlin) apply false
}
val versionNumber = "0.0.2.5"
group = "org.wdt.wdtc"
version = "$versionNumber-kotlin"
