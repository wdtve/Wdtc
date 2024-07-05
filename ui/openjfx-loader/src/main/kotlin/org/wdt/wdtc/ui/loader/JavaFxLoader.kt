package org.wdt.wdtc.ui.loader

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseJsonStreamToJsonObject
import org.wdt.utils.gson.parseObjectListTo
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectListGsonBuilder
import org.wdt.wdtc.core.openapi.game.toDependency
import org.wdt.wdtc.core.openapi.manager.MultithreadingManager
import org.wdt.wdtc.core.openapi.manager.SystemKind
import org.wdt.wdtc.core.openapi.manager.currentSystem
import org.wdt.wdtc.core.openapi.manager.wdtcDependencies
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File
import java.lang.module.Configuration
import java.lang.module.ModuleFinder
import java.net.URL

val openJFXListFile = File(wdtcDependencies, "openjfx-list.json")
val osName: String
	get() {
		require(currentSystem != SystemKind.MAC) { "Wdtc cannot run on macos!" }
		return if (currentSystem == SystemKind.WINDOWS) "win" else "linux"
	}
val canRunOSList = listOf("win", "linux")

@Suppress("unused")
suspend fun setJavaFXListJson() {
	val moduleList = run {
		listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media")
	}
	openJFXListFile.writeObjectToFile(serializeLibraryObjectListGsonBuilder) {
		canRunOSList.associateWith { os ->
			moduleList.mapTo(LibraryObjectList()) { moudleName ->
				val path = moudleName.replace(".", "-").let {
					"org/openjfx/$it/17.0.6/$it-17.0.6-$os.jar"
				}
				val url = URL("https://maven.aliyun.com/repository/public/$path")
				val artifact = runOnIO {
					val connection = async { url.openConnection() }
					LibraryObject.Artifact(
						url = url,
						path = path,
						sha1 = connection.await().getInputStream().use { it.sha1() },
						size = connection.await().contentLengthLong,
					)
				}
				LibraryObject(
					downloads = LibraryObject.Downloads(artifact = artifact),
					libraryName = "org.openjfx:$moudleName:win:17.0.6".toDependency()
				)
			}
		}
	}
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun downloadDependencies() = runOnIO {
	runtimeList.size.let {
		MultithreadingManager(it).apply {
			context = newFixedThreadPoolContext(it, "download jar")
		}
	}.doExecuters {
		runtimeList.map {
			it.downloads.artifact.let { artifact ->
				createDownloadTask(artifact) {
					artifact.run { url to wdtcDependencies.resolve(path) }
				}
			}
		}
	}
}

private val runtimeList: LibraryObjectList by lazy {
	getResourceAsStream("/openjfx-list.json").use {
		it.parseJsonStreamToJsonObject()
	}.getJsonArray(osName).parseObjectListTo(LibraryObjectList(), serializeLibraryObjectListGsonBuilder)
}

fun loadJavaFXPatch() {
	try {
		Class.forName("javafx.application.Application")
	} catch (e: ClassNotFoundException) {
		
		logmaker.info("Load JavaFX Dependencies")
		logmaker.info("OpenJFX Platform:$osName")
		
		val jars = runtimeList.map {
			wdtcDependencies.resolve(it.downloads.artifact.path).toPath()
		}
		
		val modules = runtimeList.map {
			it.libraryName.artifactId
		}
		
		// Form : HMCL3
		val finder = ModuleFinder.of(*jars.toTypedArray()).also {
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

