package org.wdt.wdtc.core.download.fabric

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
import java.io.IOException

class FabricInstallTask(launcher: Launcher, fabricVersionNumber: String?) :
  FabricDonwloadInfo(launcher, fabricVersionNumber), InstallTaskInterface {
  private val source: DownloadSourceInterface = downloadSource

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
    launcher,
    versionJsonObjectInterface.versionNumber
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
    val patches: MutableList<JsonObject> = ArrayList()
    patches.add(JsonUtils.getJsonObject(launcher.versionJson))
    patches.add(JsonUtils.getJsonObject(fabricVersionJson))
    gameVersionJsonObject.patches = patches
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