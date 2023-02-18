plugins {
    id("java")
    id("idea")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

version = "1.0-demo"

javafx {
    version = "17.0.6"
    modules("javafx.controls", "javafx.fxml")
}
repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}
//applicationDefaultJvmArgs = ["--module-path \"javafx-sdk-17.0.6/lib\" --add-modules javafx.controls,javafx.fxml"]

dependencies {
    implementation("org.openjfx:javafx-controls:17")
    implementation("org.openjfx:javafx-base:17")
    implementation("org.openjfx:javafx-fxml:17")
    implementation("org.openjfx:javafx-graphics:17")
    implementation("commons-io:commons-io:2.11.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.2.7")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}