plugins {
    id("java")
    id("idea")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}
allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        mavenLocal()
        mavenCentral()
    }
}


javafx {
    version = "19.0.2.1"
    modules("javafx.controls", "javafx.fxml", "javafx.web")
}
sourceSets {
    main {
        java {
            srcDir("src/java")
        }
        resources {
            srcDir("src/resources")
        }
    }
}
dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("log4j:log4j:1.2.17")
    implementation("com.github.axet:wget:1.7.0")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.25")
    implementation(project(":WdtcCore"))
    // https://mvnrepository.com/artifact/com.atlassian.commonmark/commonmark
    implementation("com.atlassian.commonmark:commonmark:0.17.0")
    // https://mvnrepository.com/artifact/com.jfoenix/jfoenix
    implementation("com.jfoenix:jfoenix:9.0.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}