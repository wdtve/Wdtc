@file:JvmName("JavaFxUtils")

package org.wdt.wdtc.ui

import com.google.gson.JsonArray
import jdk.internal.loader.BuiltinClassLoader
import org.wdt.utils.gson.Json
import org.wdt.utils.gson.JsonUtils
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.FilenameUtils
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.isFileNotExistsAndIsNotSameSize
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObject.Companion.getLibraryObject
import org.wdt.wdtc.core.manger.wdtcImplementationPath
import org.wdt.wdtc.core.manger.wtdcOpenJFXPath
import org.wdt.wdtc.core.utils.dependency.DependencyDownload
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.startDownloadTask
import java.io.File
import java.io.IOException
import java.lang.module.Configuration
import java.lang.module.ModuleFinder
import java.net.URL
import java.nio.file.Path
import java.util.*
import kotlin.concurrent.thread

fun setJavaFXListJson() {
  try {
    val openJfxListFile = File(wdtcImplementationPath, "openjfx-list.json")
    if (FileUtils.isFileNotExistsAndIsNotSameSize(openJfxListFile, 2393)) {
      val moudleList =
        listOf("javafx.base", "javafx.controls", "javafx.fxml", "javafx.web", "javafx.graphics", "javafx.media")
      val array = JsonArray()
      for (s in moudleList) {
        val cleanedStr = s.replace(".", "-")
        val path = "org/openjfx/$cleanedStr/17.0.6/$cleanedStr-17.0.6-win.jar"
        val url = URL("https://maven.aliyun.com/repository/public/$path")
        val artifact = LibraryObject.Artifact()
        artifact.url = url
        artifact.path = FilenameUtils.separatorsToUnix(path)
        artifact.sha1 = IOUtils.getInputStreamSha1(url.openStream())
        artifact.size = url.openConnection().contentLength.toLong()
        val downloads = LibraryObject.Downloads()
        downloads.artifact = artifact
        val libraryObject = LibraryObject()
        libraryObject.downloads = downloads
        libraryObject.libraryName = "org.openjfx:$s:win:17.0.6"
        array.add(Json.GSON.toJsonTree(libraryObject, LibraryObject::class.java))
      }
      JsonUtils.writeObjectToFile(openJfxListFile, array)
    }
  } catch (e: IOException) {
    logmaker.warn(e.getExceptionMessage())
  }
}

@Throws(IOException::class)
fun downloadDependencies() {
  val array = arrayObject
  val speed = SpeedOfProgress(array.size())
  for (i in 0 until array.size()) {
    val libraryObject = getLibraryObject(array[i].asJsonObject)
    val artifact = libraryObject.downloads?.artifact
    val library = File(wtdcOpenJFXPath, artifact?.path!!)
    thread(name = library.name) {
      try {
        if (library.isFileNotExistsAndIsNotSameSize(artifact.size)) {
          startDownloadTask(artifact.url!!, library)
        }
        speed.countDown()
      } catch (e: IOException) {
        throw RuntimeException(e)
      }
    }
  }
  speed.await()
}

@get:Throws(IOException::class)
private val arrayObject: JsonArray
  get() = IOUtils.toString(
    object {}.javaClass.getResourceAsStream("/openjfx-list.json") ?: throw RuntimeException()
  ).parseJsonArray()

fun loadJavaFXPatch() {
  try {
    Class.forName("javafx.application.Application")
  } catch (e: ClassNotFoundException) {
    try {
      logmaker.info("Load JavaFX Dependencies")
      val array = arrayObject
      val jarPaths: MutableSet<Path> = HashSet()
      val modules: MutableSet<String> = HashSet()
      for (i in 0 until array.size()) {
        val libraryObject = getLibraryObject(array.getJsonObject(i))
        val library = File(wtdcOpenJFXPath, libraryObject.downloads?.artifact?.path!!)
        jarPaths.add(library.toPath())
        modules.add(DependencyDownload(libraryObject.libraryName!!).artifactId)
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
      throw RuntimeException(ex)
    }
  }
}

fun ckeckJavaFX() {
  downloadDependencies()
  loadJavaFXPatch()
}

