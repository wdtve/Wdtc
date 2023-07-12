rootProject.name = "Wdtc-demo"
include("WdtcCore",
        "Wdtc",
        "DependencyDownloader",
        "GsonOrFastJson")

val Modules = listOf("DependencyDownloader", "GsonOrFastJson")

for (Module in Modules) {
    project(":$Module").projectDir = file("libs/$Module")
}