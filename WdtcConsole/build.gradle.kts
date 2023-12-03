plugins {
  kotlin("jvm") version "1.9.20"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.wdt.wdtc.console"
version = rootProject.version

dependencies {
  implementation("com.github.wd-t.utils:utils-io:1.2.5")
  implementation("com.github.wd-t.utils:utils-gson:1.2.5")
  implementation(project(":WdtcCore"))
  implementation("log4j:log4j:1.2.17")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("commons-cli:commons-cli:1.6.0")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  implementation(kotlin("stdlib-jdk8"))
}

val mainClazz = "org.wdt.wdtc.console.WdtcMain"
val sameManifest = mapOf(
  "Implementation-Vendor" to "Wdt~(wd-t)",
  "Implementation-Title" to "wdtc-console-java",
  "Implementation-Version" to "${project.version}",
  "Main-Class" to mainClazz,
)
tasks.jar {
  manifest {
    attributes(sameManifest)
  }
  dependsOn(tasks["shadowJar"])
}
tasks.shadowJar {
  minimize()
  manifest {
    attributes(sameManifest)
  }
}

application {
  mainClass.set(mainClazz)
  applicationDefaultJvmArgs = getJvmArgs(false)
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

tasks.create<JavaExec>("runShadowJar") {
  dependsOn(tasks.jar)
  group = "application"
  classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
  jvmArgs = getJvmArgs(true)
  workingDir = rootProject.rootDir
}


tasks.test {
  useJUnitPlatform()
}
