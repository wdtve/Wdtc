tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

group = "org.wdt.wdtc.core"
version = rootProject.version

dependencies {
    implementation("com.github.wd-t.utils:utils-io:v1.1.2.1")
    implementation(project(":DependencyDownloader"))
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