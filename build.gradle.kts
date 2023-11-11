subprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
        mavenCentral()
    }
    apply {
        plugin("java")
    }
}

group = "org.wdt.wdtc"
version = "0.0.1.15"
