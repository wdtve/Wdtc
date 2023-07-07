plugins {
    id("java")
}

group = "org.wdt"
version = "1.0"

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("dom4j:dom4j:1.6.1")
    implementation("commons-io:commons-io:2.13.0")
    implementation("log4j:log4j:1.2.17")
}

tasks.test {
    useJUnitPlatform()
}