plugins {
    id("org.openjfx.javafxplugin") version "0.0.14"
}



javafx {
    version = "19.0.2.1"
    modules("javafx.controls", "javafx.fxml")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(project(":DependencyDownloader"))
    implementation(project(":GsonOrFastJson"))
    implementation("dom4j:dom4j:1.6.1")
    implementation("commons-io:commons-io:2.13.0")
    implementation("org.hildan.fxgson:fx-gson:5.0.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.7.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}
tasks.test {
    workingDir = rootDir
    jvmArgs = listOf("-Dwdtc.config.path=.")
    useJUnitPlatform()
}