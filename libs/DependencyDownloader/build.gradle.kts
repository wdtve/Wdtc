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
    implementation("org.dom4j:dom4j:2.1.4")
    implementation("commons-io:commons-io:2.13.0")
}

tasks.test {
    useJUnitPlatform()
}