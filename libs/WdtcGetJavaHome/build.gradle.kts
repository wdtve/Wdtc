import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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
tasks.jar {
    enabled = true
    tasks.shadowJar
}
tasks.withType<ShadowJar> {
    manifest.attributes.apply {
        put("Main-Class", "org.wdt.GetJavaPath")
    }
}
dependencies {
    implementation("commons-io:commons-io:2.13.0")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.34")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
defaultTasks("shadowJar")