import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.openjfx.javafxplugin") version "0.0.14"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

javafx {
    version = "19.0.2.1"
    modules("javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics")
}

tasks.jar {
    enabled = false
    dependsOn(tasks["shadowJar"])
}


tasks.withType<ShadowJar> {
    manifest.attributes.apply {
        put("Main-Class", "org.wdt.wdtc.WdtcMain")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(project(":WdtcCore"))
    implementation(project(":GsonOrFastJson"))
    implementation("commons-io:commons-io:2.13.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.7.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    implementation("com.jfoenix:jfoenix:9.0.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.create<JavaExec>("run") {
    dependsOn(tasks.jar)
    group = "application"
    mainClass.set("org.wdt.wdtc.WdtcMain")
    jvmArgs = listOf("-Dwdtc.config.path=.", "-Ddownload.forge.true", "-Dlauncher.version=0.0.1")
    classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
    workingDir = rootProject.rootDir
}


