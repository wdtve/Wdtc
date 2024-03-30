plugins {
	alias(libs.plugins.kotlin)
}

group = "org.wdt.wdtc.ui"
version = rootProject.version

dependencies {
	implementation(project(":WdtcCore"))
	implementation(libs.coroutines.core.jvm)
	implementation(libs.utils.io)
	implementation(libs.utils.gson)
	implementation(libs.gson)
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(9)
}