package org.wdt.wdtc.core.download

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.config.GameConfig.Companion.ckeckVersionInfo
import org.wdt.wdtc.core.game.config.GameConfig.Companion.writeConfigJson
import org.wdt.wdtc.core.utils.ModUtils.modInstallTask
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.IOException

// TODO Optimize download speed
class InstallGameVersion @JvmOverloads constructor(
  launcher: Launcher, install: Boolean = false,
  var setTextFieldText: TextInterface? = null
) : DownloadGameVersion(launcher, install) {

  private val logmaker = InstallGameVersion::class.java.getWdtcLogger()
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
