subprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        mavenLocal()
        mavenCentral()
    }
    apply {
        plugin("java")
    }
}

group = "org.wdt.wdtc"
version = "0.0.1.14"
