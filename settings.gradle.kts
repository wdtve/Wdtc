plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Wdtc-demo"
include(
    "WdtcCore",
    "Wdtc",
    "WdtcConsole",
    "DependencyDownloader"
)

val modules = "DependencyDownloader"
project(":$modules").projectDir = file("libs/$modules")
