plugins {
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "org.wdt.wdtc.console"
version = rootProject.version

tasks.jar {
    enabled = false
    dependsOn(tasks["shadowJar"])
}

dependencies {
    implementation(project(":WdtcCore"))
    implementation("com.github.wd-t.utils:utils-gson:1.2.2")
    implementation("com.github.wd-t.utils:utils-io:1.2.2")
    implementation("log4j:log4j:1.2.17")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-cli:commons-cli:1.6.0")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
}

val mainClazz = "org.wdt.wdtc.console.WdtcMain"
tasks.shadowJar {
    minimize()
    manifest {
        attributes(
            "Main-Class" to mainClazz,
        )
    }
}

application {
    mainClass.set(mainClazz)
    applicationDefaultJvmArgs = listOf(
        "-Dwtdc.application.type=console",
        "-Dwdtc.launcher.version=${project.version}"
    )
}


tasks.create<JavaExec>("runShadowJar") {
    dependsOn(tasks.jar)
    group = "application"
    classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
    jvmArgs = listOf(
        "-Dwdtc.debug.switch=true",
        "-Dwtdc.application.type=console",
        "-Dwdtc.launcher.version=${project.version}"
    )
    args = listOf("-d")
    workingDir = rootProject.rootDir
}


tasks.test {
    useJUnitPlatform()
}
