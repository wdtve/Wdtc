package org.wdt.wdtc.core.download.quilt

import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException

class QuiltInstallTask : QuiltDownloadInfo, ModInstallTaskInterface {

  constructor(version: Version, quiltVersionNumber: String) : super(version, quiltVersionNumber)

  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : super(
    version,
    versionsJsonObjectInterface
  )

  private fun startDownloadQuiltGameVersionJson() {
    startDownloadTask(quiltVersionJsonUrl, quiltVersionJson)
  }

  @Throws(IOException::class)
  override fun overwriteVersionJson() {
    val gameVersionJsonObject = version.gameVersionJsonObject
    val quiltVersionJsonObject = quiltGameVersionJsonObject
    val quiltVersionJsonArguments = quiltVersionJsonObject.getJsonObject("arguments")
    val gameVersionJsonArguments = gameVersionJsonObject.arguments
    val gameVersionGameList = gameVersionJsonArguments.gameList!!
    gameVersionJsonObject.mainClass = quiltVersionJsonObject.getString("mainClass")
    gameVersionGameList.addAll(quiltVersionJsonArguments.getJsonArray("game").asJsonArray)
    gameVersionJsonArguments.gameList = gameVersionGameList
    gameVersionJsonObject.arguments = gameVersionJsonArguments
    val quiltLibraryList = quiltVersionJsonObject.getJsonArray("library")
    val gameVersionLibraryList = gameVersionJsonObject.libraries
    quiltLibraryList.forEach {
      val quiltLibraryObject = it.asJsonObject
      val download = GameRuntimeDependency(quiltLibraryObject.getString("name"))
      val libraryDefaultUrl = quiltLibraryObject.getString("url")
      if (libraryDefaultUrl == "https://maven.fabricmc.net/") {
        download.libraryRepositoriesUrl = downloadSource.fabricLibraryUrl.toURL()
      } else if (libraryDefaultUrl == "https://maven.quiltmc.org/repository/release/") {
        download.libraryRepositoriesUrl = "https://maven.quiltmc.org/repository/release/".toURL()
      }
      gameVersionLibraryList.add(LibraryObject.getLibraryObject(download, libraryDefaultUrl.toURL()))
    }
    gameVersionJsonObject.libraries = gameVersionLibraryList
    gameVersionJsonObject.id = "${version.versionNumber}-quilt-$modVersion"
    version.run {
      gameVersionJsonObject.putToVersionJson()
    }
  }

  @Throws(IOException::class)
  override fun writeVersionJsonPatches() {
    version.run {
      gameVersionJsonObject.apply {
        patches = mutableListOf(
          versionJson.readFileToJsonObject(),
          quiltVersionJson.readFileToJsonObject()
        )
      }.putToVersionJson()
    }
  }

  override fun afterDownloadTask() {}
  override fun beforInstallTask() {
    startDownloadQuiltGameVersionJson()
  }
}
