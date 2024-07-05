plugins {
	alias(libs.plugins.kotlin)
}

group = "org.wdt.wdtc.ui.loader"
version = rootProject.version

dependencies {
	implementation(project(":wdtc-core"))
	implementation(libs.coroutines.core.jvm)
	implementation(libs.utils.io)
	implementation(libs.utils.gson)
	implementation(libs.gson)
}

tasks.test {
	useJUnitPlatform()
}
kotlin {
	jvmToolchain(9)
}

tasks.getByName<JavaCompile>(java.sourceSets.main.get().compileJavaTaskName) {
	options.compilerArgs.add("--add-exports=java.base/jdk.internal.loader=ALL-UNNAMED")
}