rootProject.name = "Wdtc-demo"
include(
    "WdtcCore",
    "Wdtc",
    "DependencyDownloader",
)

val modules = "DependencyDownloader"
project(":$modules").projectDir = file("libs/$modules")
