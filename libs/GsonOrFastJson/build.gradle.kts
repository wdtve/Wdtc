plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.wdt"

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    mavenLocal()
    mavenCentral()
}
dependencies {
    implementation("commons-io:commons-io:2.13.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}