package org.wdt.wdtc.core.download.game

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.TaskKind
import org.wdt.wdtc.core.manger.TaskManger
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.*
import java.io.File

class DownloadGameAssetsFile(private val version: Version) {
  fun startDownloadAssetsFiles() =
    version.gameAssetsListJson.readFileToJsonObject().getJsonObject("objects").asMap().run {
      val progress = SpeedOfProgress(size).apply {
        coroutineScope = executorCoroutineScope(name = "Download assets file")
      }
      keys.forEach {
        get(it).ckeckIsNull().asJsonObject.parseObject<AssetsFileData>().let { data ->
          DownloadGameAssetsFileTask(version, data, progress).run {
            start()
          }
        }
      }
      progress.await()
    }


  private class AssetsFileData(
    @SerializedName("hash")
    override val sha1: String,
    @SerializedName("size")
    override val size: Long = 0
  ) : FileData {
    val hashSplicing: String
      get() = "$hashHead/$sha1"
    private val hashHead: String
      get() = sha1.substring(0, 2)
  }

  private class DownloadGameAssetsFileTask(
    version: Version, private val data: AssetsFileData, private val progress: SpeedOfProgress
  ) : TaskManger(data.sha1, TaskKind.COROUTINES) {

    private val hashFile = File(version.gameObjects, data.hashSplicing)

    init {
      coroutinesAction = progress.coroutineScope.launch(
        actionName.toCoroutineName(),
        CoroutineStart.LAZY
      ) {
        startDownloadTask((downloadSource.assetsUrl + data.hashSplicing).toURL(), hashFile)
        synchronized(this) { progress.countDown() }
      }
    }

    override fun start() {
      if (hashFile.compareFile(data)) {
        coroutinesAction.ckeckIsNull().start()
      } else {
        progress.countDown()
      }
    }
  }
}
