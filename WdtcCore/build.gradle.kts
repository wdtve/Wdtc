plugins {
  kotlin("jvm") version "1.9.21"
}

group = "org.wdt.wdtc.core"
version = rootProject.version

dependencies {
  implementation("com.github.wd-t.utils:utils-gson:1.2.7")
  implementation("com.github.wd-t.utils:utils-io:1.2.7")
  implementation("log4j:log4j:1.2.17")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation(kotlin("test"))
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.jar {
  manifest.attributes(
    "Implementation-Vendor" to "Wdt~(wd-t)",
    "Implementation-Title" to "wdtc-core-kotlin",
    "Implementation-Version" to "${project.version}",
  )
}

fun getJvmArgs(debug: Boolean): MutableList<String> {
  val jvmList = mutableListOf(
    "-Dwtdc.application.type=console",
    "-Dwdtc.launcher.version=${project.version}"
  )
  return if (debug) {
    jvmList.add("-Dwdtc.debug.switch=true")
    jvmList
  } else {
    jvmList
  }
}
tasks.test {
  workingDir = rootDir
  jvmArgs = getJvmArgs(true)
  useJUnitPlatform()
}