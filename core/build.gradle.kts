plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.shadowJar)
}

group = "org.wdt.wdtc.core.openapi"
version = rootProject.version

dependencies {
	api(libs.utils.io)
	api(libs.utils.io.okio)
	api(libs.utils.gson)
	api(libs.gson)
	api(libs.coroutines.core.jvm)
	api(libs.okio)
	api(libs.stdlib.jdk8)
	testApi(libs.stdlib.test)
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