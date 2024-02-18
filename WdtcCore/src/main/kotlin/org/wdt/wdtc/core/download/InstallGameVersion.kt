package org.wdt.wdtc.core.download

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.config.ckeckVersionInfo
import org.wdt.wdtc.core.game.config.writeConfigJson
import org.wdt.wdtc.core.utils.error
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.modInstallTask
import java.io.IOException

// TODO Optimize download speed
class InstallGameVersion(
  private val version: Version, private val install: Boolean = false,
  private val printInfo: ((String) -> Unit)? = null
) {
  private val download: DownloadVersionGameFile = DownloadVersionGameFile(version, install)


  fun startInstallGame(): Unit = runBlocking {
    try {
      val startTime = System.currentTimeMillis()
      val task = version.modInstallTask

      download.createGameDirectories()

      launch(CoroutineName("Write and ckeck config file")) {
        version.run {
          if (versionConfigFile.isFileNotExists()) {
            writeConfigJson()
          } else {
            ckeckVersionInfo()
          }
        }
      }

      startDownloadGameFileTask()

      if (install) task?.run {
        beforInstallTask()
        writeVersionJsonPatches()
        overwriteVersionJson()
      }

      download.startDownloadLibraryFile()

      (System.currentTimeMillis() - startTime).let {
        logmaker.info("Download game runtime finish,take a period of: ${it}ms")
        printInfo?.invoke("下载游戏所需类库完成,耗时${it}ms")
      }

      if (install) task?.afterDownloadTask()

      download.startDownloadAssetsFiles()

      (System.currentTimeMillis() - startTime).let {
        logmaker.info("Download game finish,take a period of: ${it}ms")
        printInfo?.invoke("游戏下载完成,耗时${it}ms")
      }
    } catch (e: IOException) {
      logmaker.error("Download Game Error,", e)
    }
  }

  private fun startDownloadGameFileTask() = download.run {
    runBlocking(Dispatchers.IO) {
      startDownloadGameVersionJson()
      launch(CoroutineName("Download assets list json")) { startDownloadGameAssetsListJson() }
      launch(CoroutineName("Download version jar")) { startDownloadVersionJar() }
    }
  }
}

