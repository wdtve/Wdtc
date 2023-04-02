plugins {
    id("idea")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

version = "0.1-demo"

javafx {
    version = "20"
    modules("javafx.controls", "javafx.fxml")
}
allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public/") }
        mavenLocal()
        mavenCentral()
    }
}


