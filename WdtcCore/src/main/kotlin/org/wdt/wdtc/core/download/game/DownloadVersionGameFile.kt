package org.wdt.wdtc.core.download.game

import org.wdt.utils.io.isFileNotExistsAndIsNotSameSize
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionJsonObjectImpl
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.manger.DownloadSourceManger.isOfficialDownloadSource
import org.wdt.wdtc.core.manger.FileManger.versionManifestFile
import org.wdt.wdtc.core.manger.URLManger.pistonMetaMojang
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.URLUtils.toURL
import java.io.IOException

class DownloadVersionGameFile(val launcher: Launcher, val install: Boolean) {

  fun startDownloadGameVersionJson() {
    if (!install) {
      return
    }
    var notFoundVersion = true
    if (isOfficialDownloadSource()) {
      val versionJsonObjectList = GameVersionList().versionList
      for (versionJsonObject in versionJsonObjectList) {
        if (versionJsonObject is GameVersionJsonObjectImpl) {
          if (versionJsonObject.versionNumber == launcher.versionNumber) {
            startDownloadTask(versionJsonObject.versionJsonURL!!, launcher.versionJson)
            notFoundVersion = false
          }
        }
      }
      if (notFoundVersion) {
        throw VersionNotFoundException("${launcher.versionJson} not found")
      }
    } else {
      val jsonURL = downloadSource.versionClientUrl.format(launcher.versionNumber, "json").toURL()
      startDownloadTask(jsonURL, launcher.versionJson)
    }
  }

  @Throws(IOException::class)
  fun startDownloadGameAssetsListJson() {
    val fileDataObject = launcher.gameVersionJsonObject.assetIndex!!
    val listJsonURL = if (isOfficialDownloadSource())
      fileDataObject.listJsonURL
    else
      fileDataObject.listJsonURL.toString().replace(pistonMetaMojang, downloadSource.metaUrl).toURL()

    if (launcher.gameAssetsListJson.isFileNotExistsAndIsNotSameSize(fileDataObject.fileSize.toLong())) {
      startDownloadTask(listJsonURL!!, launcher.gameAssetsListJson)
    }
  }

  @Throws(IOException::class)
  fun startDownloadVersionJar() {
    val fileDataObject = launcher.gameVersionJsonObject.downloads?.client!!
    val jarUrl = if (isOfficialDownloadSource())
      fileDataObject.listJsonURL
    else
      downloadSource.versionClientUrl.format(launcher.versionNumber, "client").toURL()
    if (launcher.versionJar.isFileNotExistsAndIsNotSameSize(fileDataObject.fileSize.toLong())) {
      startDownloadTask(jarUrl!!, launcher.versionJar)
    }
  }

  val downloadGameLibraryFileTask: DownloadGameClass
    get() = DownloadGameClass(launcher)

  val downloadGameAssetsFile: DownloadGameAssetsFile
    get() = DownloadGameAssetsFile(launcher)

  companion object {
    @JvmStatic
    fun startDownloadVersionManifestJsonFile() {
      startDownloadTask(downloadSource.versionManifestUrl, versionManifestFile)
    }
  }
}
