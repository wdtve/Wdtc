plugins {
    kotlin("jvm") version "1.9.0"
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
    implementation("com.github.wd-t.utils:utils-gson:v1.1.2.1")
    implementation("com.github.wd-t.utils:utils-io:v1.1.2.1")
    implementation(project(":WdtcCore"))
    implementation("log4j:log4j:1.2.17")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-cli:commons-cli:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    implementation(kotlin("stdlib-jdk8"))
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

    workingDir = rootProject.rootDir
}


tasks.test {
    useJUnitPlatform()
}
