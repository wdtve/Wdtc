package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.dependency.DependencyDownload
import java.io.IOException

class FabricInstallTask(launcher: Launcher, fabricVersionNumber: String) :
  FabricDonwloadInfo(launcher, fabricVersionNumber), InstallTaskInterface {
  private val source: DownloadSourceInterface = downloadSource

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
    launcher,
    versionJsonObjectInterface.versionNumber!!
  )

  @Throws(IOException::class)
  override fun overwriteVersionJson() {
    val gameVersionJsonObject = launcher.gameVersionJsonObject
    val libraryObjectList = gameVersionJsonObject.libraries!!
    val fabricVersionJsonObject = fabricVersionJsonObject
    val fabricLibraryList = fabricVersionJsonObject.getJsonArray("libraries")
    for (i in 0 until fabricLibraryList.size()) {
      val libraryObject = fabricLibraryList.getJsonObject(i)
      val dependency = DependencyDownload(libraryObject.getString("name"))
      dependency.defaultUrl = source.fabricLibraryUrl
      dependency.downloadPath = launcher.gameLibraryDirectory
      libraryObjectList.add(LibraryObject.getLibraryObject(dependency, libraryObject.getString("url")))
    }
    gameVersionJsonObject.libraries = libraryObjectList
    gameVersionJsonObject.mainClass = fabricVersionJsonObject.getString("mainClass")
    val gameVersionJsonArguments = gameVersionJsonObject.arguments!!
    val fabricVersionJsonArguments = fabricVersionJsonObject.getJsonObject("arguments")
    val gameVersionJsonJvmList = gameVersionJsonArguments.jvmList!!
    gameVersionJsonJvmList.addAll(fabricVersionJsonArguments.getJsonArray("jvm"))
    gameVersionJsonArguments.jvmList = gameVersionJsonJvmList
    val gameVersionJsonGameList = gameVersionJsonArguments.gameList!!
    gameVersionJsonGameList.addAll(fabricVersionJsonArguments.getJsonArray("game"))
    gameVersionJsonArguments.gameList = gameVersionJsonGameList
    gameVersionJsonObject.arguments = gameVersionJsonArguments
    gameVersionJsonObject.id = "${launcher.versionNumber}-fabric-$modVersion"
    launcher.putToVersionJson(gameVersionJsonObject)
  }

  @Throws(IOException::class)
  override fun writeVersionJsonPatches() {
    val gameVersionJsonObject = launcher.gameVersionJsonObject
    gameVersionJsonObject.patches = mutableListOf(
      launcher.versionJson.readFileToJsonObject(),
      fabricVersionJson.readFileToJsonObject()
    )
    launcher.putToVersionJson(gameVersionJsonObject)
  }

  @Throws(IOException::class)
  override fun afterDownloadTask() {
    if (isAPIDownloadTaskNoNull) {
      apiDownloadTask?.startDownloadFabricAPI()
    }
  }

  override fun beforInstallTask() {
    startDownloadTask(
      fabricVersionFileUrl.format(launcher.versionNumber, modVersion),
      fabricVersionJson
    )
  }
}