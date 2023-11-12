package org.wdt.wdtc.core.launch

import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.download.DownloadGameVersion
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.FileManger.starterBat
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger

class LaunchGame private constructor(private val launcher: Launcher) {

  val process: Process
    get() = if (setting.console)
      Runtime.getRuntime().exec(arrayOf<String>("cmd.exe", "/C", "start", starterBat.getCanonicalPath()))
    else ProcessBuilder(starterBat.getCanonicalPath()).directory(launcher.versionDirectory).start()
  val launchProcess: LaunchProcess
    get() = LaunchProcess(process)

  companion object {
    private val logmaker = getLogger(LaunchGame::class.java)

    @JvmStatic
    fun create(launcher: Launcher): LaunchGame {
      try {
        launcher.beforLaunchTask()
        logmaker.info("Start Download")
        val gameVersion = DownloadGameVersion(launcher)
        try {
          gameVersion.startDownloadGame()
        } catch (e: Throwable) {
          logmaker.error(e.getExceptionMessage())
        }
        logmaker.info("Downloaded Finish")
        logmaker.info("Write Start Script")
        val script =
          GameJvmCommand(launcher).getCommand().append(GameCLICommand(launcher).getCommand()).toString()
        logmaker.info(script)
        starterBat.writeStringToFile(script)
        logmaker.info("Write Start Script Finish")
        logmaker.info("Start Run Start Script,Console:${setting.console}")
        logmaker.info("Launch Version: ${launcher.versionNumber} - ${launcher.kind}")
        logmaker.info(launcher.gameConfig.config)
      } catch (e: Exception) {
        logmaker.error(e.getExceptionMessage())
      }
      return LaunchGame(launcher)
    }
  }
}
