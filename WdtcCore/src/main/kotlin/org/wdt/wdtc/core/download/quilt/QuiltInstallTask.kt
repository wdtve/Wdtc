package org.wdt.wdtc.core.download.quilt

import com.google.gson.JsonObject
import org.wdt.utils.dependency.DependencyDownload
import org.wdt.utils.gson.JsonUtils
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.getString
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.IOException

class QuiltInstallTask : QuiltDownloadInfo, InstallTaskInterface {
  private val source: DownloadSourceInterface
  private val logmaker = QuiltInstallTask::class.java.getWdtcLogger()

  constructor(launcher: Launcher, quiltVersionNumber: String?) : super(launcher, quiltVersionNumber) {
    source = downloadSource
  }

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : super(
    launcher,
    versionJsonObjectInterface
  ) {
    source = downloadSource
  }

  fun startDownloadQuiltGameVersionJson() {
    startDownloadTask(quiltVersionJsonUrl, quiltVersionJson)
  }

  @Throws(IOException::class)
  override fun overwriteVersionJson() {
    val gameVersionJsonObject = launcher.gameVersionJsonObject
    val quiltVersionJsonObject = quiltGameVersionJsonObject
    val quiltVersionJsonArguments = quiltVersionJsonObject.getJsonObject("arguments")
    val gameVersionJsonArguments = gameVersionJsonObject.arguments!!
    val gameVersionGameList = gameVersionJsonArguments.gameList!!
    gameVersionJsonObject.mainClass = quiltVersionJsonObject.getString("mainClass")
    gameVersionGameList.addAll(quiltVersionJsonArguments.getJsonArray("game").asJsonArray)
    gameVersionJsonArguments.gameList = gameVersionGameList
    gameVersionJsonObject.arguments = gameVersionJsonArguments
    val quiltLibraryList = quiltVersionJsonObject.getJsonArray("library")
    val gameVersionLibraryList: MutableList<LibraryObject> = gameVersionJsonObject.libraries!!
    for (i in 0 until quiltLibraryList.size()) {
      val quiltLibraryObject = quiltLibraryList.getJsonObject(i)
      val download = DependencyDownload(quiltLibraryObject.getString("name"))
      val libraryDefaultUrl = quiltLibraryObject.getString("url")
      if (libraryDefaultUrl == "https://maven.fabricmc.net/") {
        download.defaultUrl = source.fabricLibraryUrl
      } else if (libraryDefaultUrl == "https://maven.quiltmc.org/repository/release/") {
        download.defaultUrl = "https://maven.quiltmc.org/repository/release/"
      }
      gameVersionLibraryList.add(LibraryObject.getLibraryObject(download, libraryDefaultUrl))
    }
    gameVersionJsonObject.libraries = gameVersionLibraryList
    gameVersionJsonObject.id = "${launcher.versionNumber}-quilt-$modVersion"
    launcher.putToVersionJson(gameVersionJsonObject)
  }

  @Throws(IOException::class)
  override fun writeVersionJsonPatches() {
    val versionJsonObject = launcher.gameVersionJsonObject
    val patches: MutableList<JsonObject> = ArrayList()
    patches.add(JsonUtils.getJsonObject(launcher.versionJson))
    patches.add(JsonUtils.getJsonObject(quiltVersionJson))
    versionJsonObject.patches = patches
    launcher.putToVersionJson(versionJsonObject)
  }

  override fun afterDownloadTask() {}
  override fun beforInstallTask() {
    startDownloadQuiltGameVersionJson()
  }
}
