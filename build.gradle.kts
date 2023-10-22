subprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        mavenLocal()
        mavenCentral()
    }
    apply {
        plugin("idea")
        plugin("java")
    }
}
