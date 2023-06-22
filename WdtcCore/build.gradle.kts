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
    implementation("dom4j:dom4j:1.6.1")
    implementation("commons-io:commons-io:2.11.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.7.0")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.34")
    implementation(files("../libs/WdtcGetJavaHome.jar"))
    // https://mvnrepository.com/artifact/commons-httpclient/commons-httpclient
    implementation("commons-httpclient:commons-httpclient:3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}