package org.wdt.wdtc.core.download.quilt

import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.dependency.DependencyDownload
import org.wdt.wdtc.core.utils.startDownloadTask
import java.io.IOException

class QuiltInstallTask : QuiltDownloadInfo, InstallTaskInterface {

  constructor(launcher: Launcher, quiltVersionNumber: String) : super(launcher, quiltVersionNumber)

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : super(
    launcher,
    versionJsonObjectInterface
  )
  private fun startDownloadQuiltGameVersionJson() {
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
        download.defaultUrl = downloadSource.fabricLibraryUrl
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
    versionJsonObject.patches = mutableListOf(
      launcher.versionJson.readFileToJsonObject(),
      quiltVersionJson.readFileToJsonObject()
    )
    launcher.putToVersionJson(versionJsonObject)
  }

  override fun afterDownloadTask() {}
  override fun beforInstallTask() {
    startDownloadQuiltGameVersionJson()
  }
}
