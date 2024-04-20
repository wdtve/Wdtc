@file:JvmName("JavaFXUtils")

package org.wdt.wdtc.ui

import jdk.internal.loader.BuiltinClassLoader
import kotlinx.coroutines.runBlocking
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseArray
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.getSha1
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectListGsonBuilder
import org.wdt.wdtc.core.openapi.game.toDependency
import org.wdt.wdtc.core.openapi.manger.*
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File
import java.io.IOException
import java.lang.module.Configuration
import java.lang.module.ModuleFinder
import java.net.URL
import java.nio.file.Path

val openJFXListFile = File(wdtcDependenciesDirectory, "openjfx-list.json")
val osName: String
	get() {
		require(currentSystem != SystemKind.MAC) { "Wdtc cannot run on macos!" }
		return if (currentSystem == SystemKind.WINDOWS) "win" else "linux"
	}
val canRunOSList = listOf("win", "linux")

fun setJavaFXListJson() {
	try {
		val moudleList =
			listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media")
		val runtimeListMap = HashMap<String, LibraryObjectList>()
		canRunOSList.forEach { os ->
			val runtimeList = LibraryObjectList().apply {
				moudleList.forEach { moudleName ->
					val path = moudleName.replace(".", "-").let {
						"org/openjfx/$it/17.0.6/$it-17.0.6-$os.jar"
					}
					val artifact = URL("https://maven.aliyun.com/repository/public/$path").let {
						LibraryObject.Artifact(
							url = it,
							path = path,
							sha1 = it.openStream().getSha1(),
							size = it.openConnection().contentLength.toLong(),
						)
					}
					LibraryObject(
						downloads = LibraryObject.Downloads(artifact = artifact),
						libraryName = "org.openjfx:$moudleName:win:17.0.6".toDependency()
					).addToList()
					
				}
			}
			runtimeListMap[os] = runtimeList
		}
		openJFXListFile.writeObjectToFile(runtimeListMap, serializeLibraryObjectListGsonBuilder)
	} catch (e: IOException) {
		logmaker.warning(e.getExceptionMessage())
	}
}

fun downloadDependencies() = runBlocking {
	runtimeList.run {
		val speed = size.let {
			ProgressManger(it).apply {
				coroutineScope = executorCoroutineScope(it, "Download javafx jar file")
			}
		}
		forEach {
			val artifact = it.downloads.artifact
			val library = File(wtdcOpenJFXDirectory, artifact.path)
			speed.run {
				finishCountDown(library compareFile artifact) {
					coroutineScope.launch(library.name) {
						startDownloadTask(artifact.url, library)
					}
				}
			}
		}
		speed.await()
	}
}

private val runtimeList: LibraryObjectList
	get() = IOUtils.toString(getResourceAsStream("/openjfx-list.json")).parseJsonObject().getJsonArray(osName)
		.parseArray(serializeLibraryObjectListGsonBuilder)

fun loadJavaFXPatch() {
	try {
		Class.forName("javafx.application.Application")
	} catch (e: ClassNotFoundException) {
		logmaker.info("Load JavaFX Dependencies")
		logmaker.info("OpenJFX Platform:$osName")
		val jarPaths: MutableSet<Path> = HashSet()
		val modules: MutableSet<String> = HashSet()
		runtimeList.forEach {
			it.run {
				File(wtdcOpenJFXDirectory, downloads.artifact.path).run {
					jarPaths.add(toPath())
				}
				modules.add(libraryName.artifactId)
			}
		}
		// Form : HMCL3
		val finder = ModuleFinder.of(*jarPaths.toTypedArray()).also {
			it.findAll().forEach { module ->
				(ClassLoader.getSystemClassLoader() as BuiltinClassLoader).loadModule(module)
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

