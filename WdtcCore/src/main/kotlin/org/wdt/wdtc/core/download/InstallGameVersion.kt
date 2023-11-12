package org.wdt.wdtc.core.download

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.config.GameConfig
import org.wdt.wdtc.core.utils.ModUtils.getModInstallTask
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.IOException

// TODO Optimize download speed
class InstallGameVersion @JvmOverloads constructor(launcher: Launcher, install: Boolean = false) :
    DownloadGameVersion(launcher, install) {

  var setTextFieldText: TextInterface? = null
  private val logmaker = InstallGameVersion::class.java.getWdtcLogger()
  fun startInstallGame() {
    try {
      val startTime = System.currentTimeMillis()
      if (launcher.versionConfigFile.isFileNotExists()) {
        GameConfig.writeConfigJson(launcher)
      } else {
        GameConfig.ckeckVersionInfo(launcher)
      }
      downloadGameFileTask()
      val task = getModInstallTask(launcher)
      if (install && task != null) {
        task.beforInstallTask()
        task.writeVersionJsonPatches()
        task.overwriteVersionJson()
      }
      downloadGameLibraryTask()
      val libraryFinishTime = "游戏所需类库下载完成,耗时:${System.currentTimeMillis() - startTime}ms"
      logmaker.info(libraryFinishTime)
      setTextFieldText?.setControl(libraryFinishTime)
      if (install && task != null) {
        task.afterDownloadTask()
      }
      downloadResourceFileTask()
      val endTime = "下载完成,耗时:${System.currentTimeMillis() - startTime}ms"
      logmaker.info(endTime)
      setTextFieldText?.setControl(endTime)
    } catch (e: IOException) {
      logmaker.error("Download Game Error,", e)
    }
  }

}
