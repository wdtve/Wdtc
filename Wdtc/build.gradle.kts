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

val mainClazz = "org.wdt.wdtc.ui.WdtcMain"
val sameManifest = mapOf(
	"Implementation-Vendor" to "Wdt~(wd-t)",
	"Implementation-Title" to "wdtc-ui-kotlin",
	"Implementation-Version" to "${project.version}",
	"Main-Class" to mainClazz,
	"Add-Opens" to listOf("java.base/jdk.internal.loader").joinToString(" ")
)


tasks.shadowJar {
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

tasks.create<JavaExec>("runShadowJar") {
	dependsOn(tasks.jar)
	group = "application"
	classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
	jvmArgs = getJvmArgs(true)
	workingDir = rootProject.rootDir
}

dependencies {
	implementation(project(":WdtcCore"))
	implementation(libs.utils.io)
	implementation(libs.utils.gson)
	implementation(libs.gson)
	implementation(libs.coroutines.core)
	implementation(libs.coroutines.core.jvm)
	implementation("com.jfoenix:jfoenix:9.0.10")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${libs.versions.kotlinx.coroutines}")
	implementation(libs.stdlib.jdk8)
	testImplementation(libs.stdlib.test)
}

fun getJvmArgs(debug: Boolean): List<String> {
	val jvmList = mutableListOf(
		"-Dwtdc.application.type=ui",
		"-Dwdtc.launcher.version=${project.version}",
		"-Dwdtc.debug.mode=true",
		"-Dwdtc.low.performance.mode=true"
	)
	return if (debug) {
		jvmList.add("-Dwdtc.debug.mode=true")
		jvmList
	} else {
		jvmList
	}
}

tasks.test {
	workingDir = rootDir
	jvmArgs = getJvmArgs(true)
	useJUnitPlatform()
}
