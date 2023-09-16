plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `java-library`
}

group = "org.wdt"
version = "1.1.0"

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":FileUtils"))
    implementation("org.hildan.fxgson:fx-gson:5.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.34")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
