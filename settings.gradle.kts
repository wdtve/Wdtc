rootProject.name = "Wdtc-demo"
include(
    "WdtcCore",
    "Wdtc",
    "DependencyDownloader",
    "GsonOrFastJson", "FileUtils"
)

val Modules = listOf("DependencyDownloader", "FileUtils", "GsonOrFastJson")

for (Module in Modules) {
    project(":$Module").projectDir = file("libs/$Module")
}