package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException

class FabricInstallTask(version: Version, fabricVersionNumber: String) :
  FabricDonwloadInfo(version, fabricVersionNumber), ModInstallTaskInterface {

  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
    version,
    versionsJsonObjectInterface.versionNumber.ckeckIsNull()
  )

  @Throws(IOException::class)
  override fun overwriteVersionJson() {
    val gameVersionJsonObject = version.gameVersionJsonObject
    val libraryObjectList = gameVersionJsonObject.libraries
    val fabricVersionJsonObject = fabricVersionJsonObject
    val fabricLibraryList = fabricVersionJsonObject.getJsonArray("libraries")
    for (i in 0 until fabricLibraryList.size()) {
      val libraryObject = fabricLibraryList.getJsonObject(i)
      val dependency = GameRuntimeDependency(libraryObject.getString("name"))
      dependency.libraryRepositoriesUrl = downloadSource.fabricLibraryUrl.toURL()
      dependency.libraryDirectory = version.gameLibraryDirectory
      libraryObjectList.add(LibraryObject.getLibraryObject(dependency, libraryObject.getString("url").toURL()))
    }
    gameVersionJsonObject.libraries = libraryObjectList
    gameVersionJsonObject.mainClass = fabricVersionJsonObject.getString("mainClass")
    val gameVersionJsonArguments = gameVersionJsonObject.arguments
    val fabricVersionJsonArguments = fabricVersionJsonObject.getJsonObject("arguments")
    val gameVersionJsonJvmList = gameVersionJsonArguments.jvmList!!
    gameVersionJsonJvmList.addAll(fabricVersionJsonArguments.getJsonArray("jvm"))
    gameVersionJsonArguments.jvmList = gameVersionJsonJvmList
    val gameVersionJsonGameList = gameVersionJsonArguments.gameList!!
    gameVersionJsonGameList.addAll(fabricVersionJsonArguments.getJsonArray("game"))
    gameVersionJsonArguments.gameList = gameVersionJsonGameList
    gameVersionJsonObject.arguments = gameVersionJsonArguments
    gameVersionJsonObject.id = "${version.versionNumber}-fabric-$modVersion"
    version.run {
      gameVersionJsonObject.putToVersionJson()
    }
  }

  @Throws(IOException::class)
  override fun writeVersionJsonPatches() {
    version.run {
      gameVersionJsonObject.apply {
        patches = mutableListOf(
          version.versionJson.readFileToJsonObject(),
          fabricVersionJson.readFileToJsonObject()
        )
      }.putToVersionJson()
    }
  }

  @Throws(IOException::class)
  override fun afterDownloadTask() {
    if (isAPIDownloadTaskNoNull) {
      apiDownloadTask.ckeckIsNull().startDownloadFabricAPI()
    }
  }

  override fun beforInstallTask() {
    startDownloadTask(
      fabricVersionFileUrl.format(version.versionNumber, modVersion),
      fabricVersionJson
    )
  }
}