rootProject.name = "wdtc"

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(
	"wdtc-core",
	"wdtc-ui",
	"wdtc-console",
)

project(":wdtc-core").projectDir = file("core")
project(":wdtc-ui").projectDir = file("ui")
project(":wdtc-console").projectDir = file("console")

include(
	":wdtc-core:core-api",
	":wdtc-core:core-impl",
)
project(":wdtc-core:core-api").projectDir = file("core/core-api")
project(":wdtc-core:core-impl").projectDir = file("core/core-impl")

include(
	":wdtc-ui:openjfx-loader"
)
project(":wdtc-ui:openjfx-loader").projectDir = file("ui/openjfx-loader")

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("./gradle/libs.version.toml"))
		}
	}
}
