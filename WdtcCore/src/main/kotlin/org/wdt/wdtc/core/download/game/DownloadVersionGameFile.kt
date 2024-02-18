package org.wdt.wdtc.core.download.game

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionsJsonObjectImpl
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.launch.GameRuntimeList
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.isOfficialDownloadSource
import org.wdt.wdtc.core.manger.pistonMetaMojang
import org.wdt.wdtc.core.manger.versionManifestFile
import org.wdt.wdtc.core.utils.*
import java.io.IOException

class DownloadVersionGameFile(val version: Version, private val install: Boolean) {

  fun startDownloadGameVersionJson() {
    if (!install && version.versionJson.isFileExists()) return
    if (isOfficialDownloadSource) {
      GameVersionList().versionList.forEach {
        if (it is GameVersionsJsonObjectImpl) {
          if (it.versionNumber == version.versionNumber) {
            startDownloadTask(it.versionJsonURL.ckeckIsNull(), version.versionJson)
            return
          }
        }
      }
      throw VersionNotFoundException("${version.versionNumber} not found")
    } else {
      downloadSource.versionClientUrl.format(version.versionNumber, "json").toURL().let {
        startDownloadTask(it, version.versionJson)
      }
    }
  }

  fun startDownloadGameAssetsListJson() {
    val data = version.gameVersionJsonObject.assetIndex
    val listJsonURL = data.url.run {
      if (isOfficialDownloadSource) this
      else toString().replace(pistonMetaMojang, downloadSource.metaUrl).toURL()
    }
    version.gameAssetsListJson.let {
      if (it.compareFile(data)) {
        startDownloadTask(listJsonURL, it)
      }
    }
  }

  fun startDownloadVersionJar() {
    val data = version.gameVersionJsonObject.downloads.client
    val jarUrl = data.let {
      if (isOfficialDownloadSource) it.url
      else downloadSource.versionClientUrl.format(version.versionNumber, "client").toURL()
    }
    version.versionJar.let {
      if (it.compareFile(data)) {
        startDownloadTask(jarUrl, it)
      }
    }
  }

  fun startDownloadLibraryFile(): Unit = runBlocking(Dispatchers.IO) {
    launch(CoroutineName("Donwnload library file")) {
      try {
        GameRuntimeList(version).runtimeList.run {
          val speed = SpeedOfProgress(size).apply {
            coroutineScope = executorCoroutineScope(name = "Download library file")
          }
          forEach {
            DownloadGameRuntime(version, it, speed).run {
              start()
            }
          }
          speed.await()
        }

      } catch (e: IOException) {
        logmaker.error("Download Library File Error,", e)
      }
    }
    launch(CoroutineName("Copy log4j file")) {
      IOUtils.copy(getResourceAsStream("/assets/log4j2.xml"), version.versionLog4j2.outputStream())
    }
  }

  fun startDownloadAssetsFiles() = DownloadGameAssetsFile(version).run {
    startDownloadAssetsFiles()
  }


  fun createGameDirectories() {
    version.run {
      gameDirectory.createDirectories()
      gameLibraryDirectory.createDirectories()
      gameObjects.createDirectories()
      gameAssetsDirectory.createDirectories()
      gameVersionsDirectory.createDirectories()
      versionDirectory.createDirectories()
      versionNativesPath.createDirectories()
      gameModsPath.createDirectories()
      gameLogDirectory.createDirectories()
    }
  }

  companion object {
    fun startDownloadVersionManifestJsonFile() {
      startDownloadTask(downloadSource.versionManifestUrl, versionManifestFile)
    }
  }
}
