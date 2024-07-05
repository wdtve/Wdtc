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

group = "org.wdt.wdtc"

val versionNumber = "0.0.2.6"
version = "$versionNumber-kotlin"
