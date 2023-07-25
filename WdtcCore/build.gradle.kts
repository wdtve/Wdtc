plugins {
    id("org.openjfx.javafxplugin") version "0.0.14"
}



javafx {
    version = "19.0.2.1"
    modules("javafx.controls", "javafx.fxml")
}
sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
        resources {
            srcDir("src/main/resources")
        }
    }
}
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
dependencies {
    implementation(project(":DependencyDownloader"))
    implementation(project(":GsonOrFastJson"))
    implementation("dom4j:dom4j:1.6.1")
    implementation("commons-io:commons-io:2.13.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.7.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-httpclient:commons-httpclient:3.1")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}
tasks.test {
    useJUnitPlatform()
}