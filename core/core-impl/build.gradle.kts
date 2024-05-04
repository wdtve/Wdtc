plugins {
	alias(libs.plugins.kotlin)
}

group = "org.wdt.wdtc.core.impl"
version = rootProject.version

dependencies {
	api(project(":wdtc-core:core-api"))
}

kotlin {
	jvmToolchain(8)
}

tasks.jar {
	manifest.attributes(
		"Implementation-Vendor" to "Wdt~(wdtve)",
		"Implementation-Title" to "wdtc-core-kotlin",
		"Implementation-Version" to "${project.version}"
	)
}

tasks.test {
	workingDir = rootDir
	jvmArgs = listOf("-Dwdtc.debug.switch=true", "-Dwdtc.config.path=./")
	useJUnitPlatform()
}