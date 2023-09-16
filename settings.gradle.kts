rootProject.name = "Wdtc-demo"
include(
    "WdtcCore",
    "Wdtc",
    "DependencyDownloader",
        "GsonOrFastJson",
        "FileUtils"
)

val Modules = listOf("DependencyDownloader", "GsonOrFastJson", "FileUtils")

for (Module in Modules) {
    project(":$Module").projectDir = file("libs/$Module")
}