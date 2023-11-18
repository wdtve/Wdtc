plugins {
  id("org.openjfx.javafxplugin") version "0.0.14"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

val moduleList = listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics")
javafx {
  version = "17.0.6"
  setPlatform("windows")
  modules = moduleList
}

group = "org.wdt.wdtc.ui"
version = rootProject.version

val mainClazz = "org.wdt.wdtc.ui.WdtcMain"
val sameManifest = mapOf(
  "Implementation-Vendor" to "Wdt~(wd-t)",
  "Implementation-Title" to "wdtc-ui-java",
  "Implementation-Version" to "${project.version}",
  "Main-Class" to mainClazz,
  "Add-Opens" to listOf("java.base/jdk.internal.loader").joinToString(" ")
)

tasks.jar {
  manifest.attributes(sameManifest)
  dependsOn(tasks["shadowJar"])
}

tasks.shadowJar {
  minimize()
  dependencies {
    for (module in moduleList) {
      exclude(dependency("org.openjfx:$module:${javafx.version}"))
    }
    exclude(dependency("org.jetbrains.kotlin:.*:.*"))
    exclude(dependency("org.dom4j:.*:.*"))
  }
  manifest.attributes(sameManifest)
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

tasks.compileJava<JavaCompile> {
  options.compilerArgs.add("--add-exports=java.base/jdk.internal.loader=ALL-UNNAMED")
}

dependencies {
  implementation(project(":WdtcCore"))
  implementation(project(":DependencyDownloader"))
  implementation("com.github.wd-t.utils:utils-io:1.2.2")
  implementation("log4j:log4j:1.2.17")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.jfoenix:jfoenix:9.0.10")
  implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
  workingDir = rootDir
  jvmArgs = getJvmArgs(true)
  useJUnitPlatform()
}


