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
    modules("javafx.controls", "javafx.fxml")
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
    // https://mvnrepository.com/artifact/org.apache.ant/ant
    implementation("org.apache.ant:ant:1.10.13")
    // https://mvnrepository.com/artifact/org.eclipse.aether/aether-api
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
    implementation(files("../libs/authlib-injector-1.2.2.jar"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}