package org.wdt.wdtc.core.download.game

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.io.isFileNotExistsAndIsNotSameSize
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.isStopDownloadProcess
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.startThread
import org.wdt.wdtc.core.utils.toURL
import java.io.File

open class DownloadGameAssetsFile(val launcher: Launcher) {
  fun startDownloadAssetsFiles() {
    val maps = launcher.gameAssetsListJson.readFileToJsonObject().getJsonObject("objects").asMap()
    val progress = SpeedOfProgress(maps.size)
    for (key in maps.keys) {
      val data: AssetsFileData = maps[key]?.asJsonObject?.parseObject()!!
      if (isStopDownloadProcess) return
      if (File(launcher.gameObjects, data.hashSplicing).isFileNotExistsAndIsNotSameSize(data.size.toLong())) {
        val task = DownloadGameAssetsFileTask(launcher, data, progress)
        task.startThread()
      } else {
        progress.countDown()
      }
    }
    progress.await()
  }

  class AssetsFileData {
    @SerializedName("hash")
    val hash: String? = null

    @SerializedName("size")
    val size = 0
    val hashSplicing: String
      get() = "$hashHead/$hash"
    val hashHead: String
      get() = hash?.substring(0, 2)!!
  }

  class DownloadGameAssetsFileTask(
    private val launcher: Launcher,
    private val data: AssetsFileData,
    private val progress: SpeedOfProgress
  ) : Thread() {
    private val source: DownloadSourceInterface = downloadSource

    override fun run() {
      val hashFile = File(launcher.gameObjects, data.hashSplicing)
      val hashUrl = (source.assetsUrl + data.hashSplicing).toURL()
      startDownloadTask(hashUrl, hashFile)
      synchronized(this) { progress.countDown() }
    }
  }
}
