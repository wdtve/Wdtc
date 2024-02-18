plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.shadowJar)
}

group = "org.wdt.wdtc.console"
version = rootProject.version

dependencies {
  implementation(project(":WdtcCore"))
  implementation("com.github.wd-t.utils:utils-cli:1.3.3")
  implementation(libs.utils.io)
  implementation(libs.utils.gson)
  implementation(libs.gson)
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.core.jvm)
  implementation(libs.stdlib.jdk8)
  testImplementation(libs.stdlib.test)
}

val mainClazz = "org.wdt.wdtc.console.WdtcMain"
val sameManifest = mapOf(
  "Implementation-Vendor" to "Wdt~(wd-t)",
  "Implementation-Title" to "wdtc-console-kotlin",
  "Implementation-Version" to "${project.version}",
  "Main-Class" to mainClazz,
)
tasks.shadowJar {
  minimize()
  manifest.attributes(sameManifest)
}
tasks.jar {
  manifest.attributes(sameManifest)
  dependsOn(tasks["shadowJar"])
}

application {
  mainClass.set(mainClazz)
  applicationDefaultJvmArgs = getJvmArgs(false)
}


tasks.create<JavaExec>("runShadowJar") {
  dependsOn(tasks.jar)
  group = "application"
  classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
  jvmArgs = getJvmArgs(true)
  workingDir = rootProject.rootDir
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
  jvmArgs = getJvmArgs(true)
  useJUnitPlatform()
}
