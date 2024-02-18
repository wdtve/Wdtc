package org.wdt.wdtc.core.launch

import kotlinx.coroutines.runBlocking
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.config.gameConfig
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.starterBat
import org.wdt.wdtc.core.utils.error
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.info
import org.wdt.wdtc.core.utils.logmaker
import kotlin.system.measureTimeMillis

class LaunchGame private constructor(private val version: Version) {

  val launchTaskProcess: ProcessBuilder
    get() = if (currentSetting.console)
      ProcessBuilder("cmd.exe", "/C", "start", starterBat.canonicalPath).directory(version.versionDirectory)
    else
      ProcessBuilder(starterBat.canonicalPath).directory(version.versionDirectory)


  companion object {

    fun create(version: Version): LaunchGame = runBlocking {
      try {
        val intend = measureTimeMillis {
          version.beforLaunchTask()
          logmaker.info("Start Download")
          try {
            InstallGameVersion(version, false).run {
              startInstallGame()
            }
          } catch (e: Throwable) {
            logmaker.error(e.getExceptionMessage())
          }
          logmaker.info("Downloaded Finish")
          logmaker.info("Write Start Script")
          buildString {
            append(GameJvmCommand(version).getCommand())
            append(GameCLICommand(version).getCommand())
          }.let {
            logmaker.info(it)
            starterBat.writeStringToFile(it)
          }
          logmaker.info("Write Start Script Finish")
        }
        logmaker.info("Intend time: $intend ms")
        logmaker.info("Start Run Start Script,Console:${currentSetting.console}")
        logmaker.info("Launch Version: ${version.versionNumber} - ${version.kind}")
        logmaker.info(version.gameConfig.config)
      } catch (e: Exception) {
        logmaker.error(e.getExceptionMessage())
      }
      LaunchGame(version)
    }

    private fun Version.beforLaunchTask() {
      if (!currentSetting.chineseLanguage) return
      gameOptionsFile.run {
        if (isFileNotExists()) {
          writeStringToFile("forceUnicodeFont:true\nguiScale:2\nlang:zh_cn\nautoJump:false")
        }
      }
    }
  }
}
