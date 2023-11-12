package org.wdt.wdtc.core.game.config

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.utils.WdtcLogger
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.IOException

class GameConfig(private val launcher: Launcher) {
  val config: DefaultGameConfig.Config?
    get() = defaultGameConfig.config

  val defaultGameConfig: DefaultGameConfig
    get() = launcher.versionConfigFile.readFileToClass()
  val versionInfo: VersionInfo?
    get() = defaultGameConfig.info

  @Throws(IOException::class)
  fun putConfigToFile(config: DefaultGameConfig) {
    launcher.versionConfigFile.writeStringToFile(
      Json.GSON_BUILDER.serializeNulls().setPrettyPrinting().create().toJson(config)
    )
  }

  companion object {
    private val logmaker = getLogger(WdtcLogger::class.java)

    @JvmStatic
    @Throws(IOException::class)
    fun writeConfigJsonToAllVersion() {
      val gameDirectoryManger = GameDirectoryManger()
      if (DownloadedGameVersion.isDownloadedGame(gameDirectoryManger)) {
        val list = DownloadedGameVersion.getGameVersionList(gameDirectoryManger)
        for (child in list!!) {
          val config = child.gameConfig
          if (child.versionConfigFile.isFileNotExists()) {
            writeConfigJson(child)
          } else {
            val gameConfig = config.defaultGameConfig
            gameConfig.info = config.versionInfo
            logmaker.info(gameConfig)
            config.putConfigToFile(gameConfig)
          }
        }
      } else {
        logmaker.warn("There are currently no game versions available")
      }
    }

    fun writeConfigJson(launcher: Launcher) {
      try {
        val config = DefaultGameConfig(launcher)
        launcher.versionConfigFile.writeStringToFile(
          Json.GSON_BUILDER.serializeNulls().setPrettyPrinting().create().toJson(config)
        )
        logmaker.info("${launcher.versionNumber} $config")
      } catch (e: IOException) {
        logmaker.error(e.getExceptionMessage())
      }
    }

    @Throws(IOException::class)
    fun ckeckVersionInfo(launcher: Launcher) {
      val config = launcher.gameConfig.defaultGameConfig
      config.info = launcher.versionInfo
      launcher.versionConfigFile.writeStringToFile(
        Json.GSON_BUILDER.serializeNulls().setPrettyPrinting().create().toJson(config)
      )
      logmaker.info("${launcher.versionNumber} $config")
    }
  }
}
