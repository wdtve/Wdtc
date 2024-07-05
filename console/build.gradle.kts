plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.shadowJar)
}

group = "org.wdt.wdtc.console"
version = rootProject.version

dependencies {
	api(project(":wdtc-core"))
}

val mainClazz = "org.wdt.wdtc.console.WdtcMain"
val sameManifest = mapOf(
	"Implementation-Vendor" to "Wdt~(wdtev)",
	"Implementation-Title" to "wdtc-console-kotlin",
	"Implementation-Version" to "${project.version}",
	"Main-Class" to mainClazz,
)
tasks.shadowJar {
	minimize()
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


tasks.create<JavaExec>("runConsole") {
	dependsOn(tasks.jar)
	group = "application"
	classpath = files(tasks.shadowJar.get().archiveFile.get().asFile)
	jvmArgs = getJvmArgs(true)
	workingDir = rootProject.rootDir
}

fun getJvmArgs(debug: Boolean): MutableList<String> {
	return mutableListOf(
		"-Dwtdc.application.type=console",
		"-Dwdtc.launcher.version=${project.version}"
	).also {
		if (debug) {
			it.add("-Dwdtc.debug.switch=true")
		}
	}
}

tasks.test {
	jvmArgs = getJvmArgs(true)
	useJUnitPlatform()
}
