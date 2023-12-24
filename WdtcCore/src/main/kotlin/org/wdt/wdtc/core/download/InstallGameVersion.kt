package org.wdt.wdtc.core.download

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.config.ckeckVersionInfo
import org.wdt.wdtc.core.game.config.writeConfigJson
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.modInstallTask
import java.io.IOException

// TODO Optimize download speed
class InstallGameVersion @JvmOverloads constructor(
  launcher: Launcher, install: Boolean = false,
  private val printInfo:((String) -> Unit)? = null
) : DownloadGameVersion(launcher, install) {

  fun startInstallGame() {
    try {
      val startTime = System.currentTimeMillis()
      if (launcher.versionConfigFile.isFileNotExists()) {
        launcher.writeConfigJson()
      } else {
        launcher.ckeckVersionInfo()
      }
      downloadGameFileTask()
      val task = launcher.modInstallTask
      if (install) {
        task?.beforInstallTask()
        task?.writeVersionJsonPatches()
        task?.overwriteVersionJson()
      }
      downloadGameLibraryTask()
      val libraryFinishTime = "游戏所需类库下载完成,耗时:${System.currentTimeMillis() - startTime}ms"
      logmaker.info(libraryFinishTime)
      printInfo?.let { it(libraryFinishTime) }
      if (install) task?.afterDownloadTask()
      downloadResourceFileTask()
      val endTime = "下载完成,耗时:${System.currentTimeMillis() - startTime}ms"
      logmaker.info(endTime)
      printInfo?.let { it(endTime) }
    } catch (e: IOException) {
      logmaker.error("Download Game Error,", e)
    }
  }

}
