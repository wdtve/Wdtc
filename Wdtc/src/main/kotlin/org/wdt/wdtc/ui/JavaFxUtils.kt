@file:JvmName("JavaFXUtils")

package org.wdt.wdtc.ui

import jdk.internal.loader.BuiltinClassLoader
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseArray
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.getSha1
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObjectList
import org.wdt.wdtc.core.game.serializeLibraryObjectListGsonBuilder
import org.wdt.wdtc.core.game.toDependency
import org.wdt.wdtc.core.manger.SystemKind.MAC
import org.wdt.wdtc.core.manger.SystemKind.WINDOWS
import org.wdt.wdtc.core.manger.currentSystem
import org.wdt.wdtc.core.manger.wdtcDependenciesDirectory
import org.wdt.wdtc.core.manger.wtdcOpenJFXDirectory
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.ui.window.setErrorWin
import java.io.File
import java.io.IOException
import java.lang.module.Configuration
import java.lang.module.ModuleFinder
import java.net.URL
import java.nio.file.Path

val openJFXListFile = File(wdtcDependenciesDirectory, "openjfx-list.json")
val osName: String
  get() {
    if (currentSystem == MAC) error("Wdtc cannot run on macos!")
    return if (currentSystem == WINDOWS) "win" else "linux"
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
            downloads = LibraryObject.Downloads(
              artifact = artifact
            ), libraryName = "org.openjfx:$moudleName:win:17.0.6".toDependency()
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
    val speed = SpeedOfProgress(size).apply {
      coroutineScope = executorCoroutineScope(size, "Download javafx jar file")
    }
    forEach {
      val artifact = it.downloads.artifact
      val library = File(wtdcOpenJFXDirectory, artifact.path)
      speed.coroutineScope.launch {
        try {
          if (library.compareFile(artifact)) {
            startDownloadTask(artifact.url, library)
            speed.countDown()
          } else {
            speed.countDown()
          }
        } catch (e: IOException) {
          setErrorWin(e)
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
    try {
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
      val finder = ModuleFinder.of(*jarPaths.toTypedArray())
      for (mref in finder.findAll()) {
        (ClassLoader.getSystemClassLoader() as BuiltinClassLoader).loadModule(mref)
      }
      val config = Configuration.resolveAndBind(finder, listOf(ModuleLayer.boot().configuration()), finder, modules)
      ModuleLayer.defineModules(
        config, listOf(ModuleLayer.boot())
      ) { ClassLoader.getSystemClassLoader() }
      logmaker.info("Done")
    } catch (ex: IOException) {
      setErrorWin(ex)
    }
  }
}

fun ckeckJavaFX() {
  downloadDependencies()
  loadJavaFXPatch()
}

