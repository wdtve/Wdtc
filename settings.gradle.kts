rootProject.name = "Wdtc-demo"
include(
  "WdtcCore",
  "Wdtc",
  "WdtcConsole",
)
dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("./libs.version.toml"))
    }
  }
}