package org.wdt.wdtc.ui

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseJsonStreamToJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.LibraryObjectList.Companion.asLibraryObjectList
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectListGsonBuilder
import org.wdt.wdtc.core.openapi.game.toDependency
import org.wdt.wdtc.core.openapi.manger.ProgressManger
import org.wdt.wdtc.core.openapi.manger.SystemKind
import org.wdt.wdtc.core.openapi.manger.currentSystem
import org.wdt.wdtc.core.openapi.manger.wdtcDependenciesDirectory
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File
import java.lang.module.Configuration
import java.lang.module.ModuleFinder
import java.net.URL

val openJFXListFile = File(wdtcDependenciesDirectory, "openjfx-list.json")
val osName: String
	get() {
		require(currentSystem != SystemKind.MAC) { "Wdtc cannot run on macos!" }
		return if (currentSystem == SystemKind.WINDOWS) "win" else "linux"
	}
val canRunOSList = listOf("win", "linux")

suspend fun setJavaFXListJson() {
	val moudleList = run {
		listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media")
	}
	openJFXListFile.writeObjectToFile(serializeLibraryObjectListGsonBuilder) {
		canRunOSList.associateWith { os ->
			moudleList.map { moudleName ->
				val path = moudleName.replace(".", "-").let {
					"org/openjfx/$it/17.0.6/$it-17.0.6-$os.jar"
				}
				val url = URL("https://maven.aliyun.com/repository/public/$path")
				val connection = runOnIO { async { url.openConnection() } }
				val artifact = LibraryObject.Artifact(
					url = url,
					path = path,
					sha1 = runOnIO { connection.await().getInputStream() }.use { it.sha1() },
					size = connection.await().contentLengthLong,
				)
				LibraryObject(
					downloads = LibraryObject.Downloads(artifact = artifact),
					libraryName = "org.openjfx:$moudleName:win:17.0.6".toDependency()
				)
			}.asLibraryObjectList()
		}
		
	}
}

suspend fun downloadDependencies() = runOnIO {
	val speed = runtimeList.size.let {
		ProgressManger(it).apply {
			coroutineScope = executorCoroutineScope(it, "Download Jar file")
		}
	}
	runtimeList.forEach {
		val artifact = it.downloads.artifact
		val library = wdtcDependenciesDirectory.resolve(artifact.path)
		
		speed.run {
			coroutineScope.launch {
				downloadFinishCountDown(artifact) {
					artifact.url to library
				}
			}
		}
	}
	speed.await()
}

private val runtimeList: LibraryObjectList by lazy {
	getResourceAsStream("/openjfx-list.json").use {
		it.parseJsonStreamToJsonObject()
	}.getJsonArray(osName).parseObject(serializeLibraryObjectListGsonBuilder)
}

fun loadJavaFXPatch() {
	try {
		Class.forName("javafx.application.Application")
	} catch (e: ClassNotFoundException) {
		logmaker.info("Load JavaFX Dependencies")
		logmaker.info("OpenJFX Platform:$osName")
		val jarPaths = runtimeList.map {
			wdtcDependenciesDirectory.resolve(it.downloads.artifact.path).toPath()
		}
		val modules = runtimeList.map {
			it.libraryName.artifactId
		}
		// Form : HMCL3
		val finder = ModuleFinder.of(*jarPaths.toTypedArray()).also {
			it.findAll().forEach { module ->
				JavaFxLoader.loadJavaFXPatch(module)
			}
		}
		val config = finder.let {
			Configuration.resolveAndBind(it, listOf(ModuleLayer.boot().configuration()), it, modules)
		}
		ModuleLayer.defineModules(config, listOf(ModuleLayer.boot())) {
			ClassLoader.getSystemClassLoader()
		}
		
		logmaker.info("Done")
	}
}

