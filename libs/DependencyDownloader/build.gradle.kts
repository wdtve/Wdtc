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
    // https://mvnrepository.com/artifact/dom4j/dom4j
    implementation("dom4j:dom4j:1.6.1")
    implementation("commons-io:commons-io:2.13.0")
    implementation("log4j:log4j:1.2.17")
    implementation("org.eclipse.aether:aether-api:1.1.0")
    // https://mvnrepository.com/artifact/org.eclipse.aether/aether-util
    implementation("org.eclipse.aether:aether-util:1.1.0")
    implementation("org.eclipse.aether:aether-impl:1.1.0")
    implementation("org.eclipse.aether:aether-connector-basic:1.1.0")
    implementation("org.eclipse.aether:aether-transport-http:1.1.0")
    implementation("org.eclipse.aether:aether-transport-wagon:1.1.0")
    implementation("org.eclipse.aether:aether-transport-file:1.1.0")
    // https://mvnrepository.com/artifact/org.apache.maven/maven-aether-provider
    implementation("org.apache.maven:maven-aether-provider:3.3.9")
    // https://mvnrepository.com/artifact/org.apache.maven.wagon/wagon-ssh
    implementation("org.apache.maven.wagon:wagon-ssh:3.5.3")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}