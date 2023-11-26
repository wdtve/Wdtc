plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.wdt.wdtc.console"
version = rootProject.version

dependencies {
    implementation(project(":WdtcCore"))
  implementation("com.github.wd-t.utils:utils-gson:1.2.4")
  implementation("com.github.wd-t.utils:utils-io:1.2.4")
    implementation("log4j:log4j:1.2.17")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-cli:commons-cli:1.6.0")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
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
    args = listOf("-d")
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
    useJUnitPlatform()
}
