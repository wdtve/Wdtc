plugins {
    id("org.openjfx.javafxplugin") version "0.0.14"
}

javafx {
    version = "17.0.6"
    setPlatform("windows")
    modules("javafx.controls", "javafx.fxml")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(project(":DependencyDownloader"))
    implementation(files("../libs/IOUtils-1.1.1.jar"))
    implementation("org.dom4j:dom4j:2.1.4")
    implementation("org.hildan.fxgson:fx-gson:5.0.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}
tasks.test {
    workingDir = rootDir
    jvmArgs = listOf("-Dwdtc.config.path=.")
    useJUnitPlatform()
}