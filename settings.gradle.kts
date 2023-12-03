rootProject.name = "Wdtc-demo"
include(
    "WdtcCore",
    "Wdtc",
    "WdtcConsole",
    "DependencyDownloader"
)

val modules = "DependencyDownloader"
project(":$modules").projectDir = file("libs/$modules")