plugins {
	id("org.openjfx.javafxplugin") version "0.1.0"
	alias(libs.plugins.kotlin)
	alias(libs.plugins.shadowJar)
}
val moduleList = listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics")
javafx {
	version = "17.0.6"
	modules = moduleList
}

group = "org.wdt.wdtc.ui"
version = rootProject.version

val resourceFolder = file("./build/resources/main")
val versionFile = resourceFolder.resolve("version.txt").also {
	if (!it.exists()) {
		it.createNewFile()
	}
	it.writeText(project.version.toString())
}

val dependencies = resourceFolder.resolve("libs.version.toml").also {
	if (it.exists()) {
		it.delete()
	}
	rootDir.resolve("gradle/libs.version.toml").copyTo(it)
}

val mainClazz = "org.wdt.wdtc.ui.WdtcMain"
val sameManifest = mapOf(
	"Implementation-Vendor" to "Wdt~(wdtve)",
	"Implementation-Title" to "wdtc-ui-kotlin",
	"Implementation-Version" to "${project.version}",
	"Main-Class" to mainClazz,
	"Add-Opens" to listOf("java.base/jdk.internal.loader").joinToString(" ")
)

dependencies {
	api(project(":wdtc-core"))
	api(project(":wdtc-ui:openjfx-loader"))
	implementation("com.jfoenix:jfoenix:9.0.10")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${libs.versions.kotlinx.coroutines}")
	testImplementation(libs.stdlib.test)
}

kotlin {
	jvmToolchain(9)
}

tasks.shadowJar {
	append(versionFile.name)
	append(dependencies.name)
	minimize()
	dependencies {
		moduleList.forEach {
			exclude(dependency("org.openjfx:$it:${javafx.version}"))
		}
	}
	manifest.attributes(sameManifest)
}

tasks.jar {
	manifest.attributes(sameManifest)
	dependsOn(tasks["shadowJar"])
}

application {
	mainClass.set(mainClazz)
	applicationDefaultJvmArgs = getJvmArgs(false)
}

tasks.create<JavaExec>("runUI") {
	dependsOn(tasks.jar)
	group = "application"
	classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
	jvmArgs = getJvmArgs(true)
	workingDir = rootProject.rootDir
}


fun getJvmArgs(debug: Boolean): List<String> {
	return mutableListOf(
		"-Dwtdc.application.type=ui",
		"-Dwdtc.launcher.version=${project.version}",
		"-Dwdtc.debug.mode=true",
		"-Dwdtc.low.performance.mode=false"
	).also {
		if (debug) {
			it.add("-Dwdtc.debug.mode=true")
		}
	}
}

tasks.test {
	workingDir = rootDir
	jvmArgs = getJvmArgs(true)
	useJUnitPlatform()
}
