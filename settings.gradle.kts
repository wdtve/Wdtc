rootProject.name = "Wdtc-demo"
include("WdtcCore",
        "Wdtc",
        "DependencyDownloader",
        "GsonOrFastJson",
        "WdtcGetJavaHome")

val Modules = listOf("DependencyDownloader", "GsonOrFastJson", "WdtcGetJavaHome")

for (Module in Modules) {
    project(":$Module").projectDir = file("libs/$Module")
}