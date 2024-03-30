plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Wdtc-demo"
include(
	"WdtcCore",
	"Wdtc",
	"WdtcConsole",
	"Wdtc:OpenJFXLoader"
)
dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("./libs.version.toml"))
		}
	}
}
include("")
findProject(":Wdtc:OpenJFXLoader")?.name = "OpenJFXLoader"
