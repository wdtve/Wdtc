plugins {
  alias(libs.plugins.kotlin)
}

group = "org.wdt.wdtc.core"
version = rootProject.version

dependencies {
  implementation(libs.utils.io)
  implementation(libs.utils.io.okio)
  implementation(libs.utils.gson)
  implementation(libs.gson)
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.core.jvm)
  implementation(libs.okio)
  implementation(libs.stdlib.jdk8)
  testImplementation(libs.stdlib.test)
}

kotlin {
	jvmToolchain(8)
}

tasks.jar {
  manifest.attributes(
    "Implementation-Vendor" to "Wdt~(wd-t)",
    "Implementation-Title" to "wdtc-core-kotlin",
    "Implementation-Version" to "${project.version}"
  )
}

tasks.test {
  workingDir = rootDir
  jvmArgs = listOf("-Dwdtc.debug.switch=true", "-Dwdtc.config.path=./")
  useJUnitPlatform()
}