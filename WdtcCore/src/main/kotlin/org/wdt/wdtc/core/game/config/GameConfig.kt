package org.wdt.wdtc.core.game.config

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.gameVersionList
import org.wdt.wdtc.core.utils.error
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.gson.prettyGsonBuilder
import org.wdt.wdtc.core.utils.logmaker
import java.io.IOException

class GameConfig(private val version: Version) {
  val config: DefaultGameConfig.Config
    get() = defaultGameConfig.config

  val defaultGameConfig: DefaultGameConfig
    get() = version.versionConfigFile.readFileToClass()
  val versionInfo: VersionInfo
    get() = defaultGameConfig.info

  fun putConfigToFile(config: DefaultGameConfig) {
    version.writeConfigJsonToFile(config)
  }
}

fun writeConfigJsonToAllVersion() {
  GameDirectoryManger().gameVersionList.run {
    if (isNotEmpty()) {
      forEach {
        it.run {
          if (versionConfigFile.isFileNotExists()) {
            writeConfigJson()
          } else {
            gameConfig.run {
              defaultGameConfig.apply {
                info = versionInfo
              }.let { config ->
                putConfigToFile(config)
              }
            }
          }
        }
      }
    } else {
      logmaker.warning("There are currently no game versions available")
    }
  }
}

fun Version.writeConfigJson() {
  this.writeConfigJsonToFile(DefaultGameConfig(this))
}

fun Version.ckeckVersionInfo() {
  this.gameConfig.defaultGameConfig.apply {
    info = this@ckeckVersionInfo.versionInfo
  }.let {
    this.writeConfigJsonToFile(it)
  }
}

val Version.gameConfig: GameConfig
  get() = GameConfig(this)

private fun Version.writeConfigJsonToFile(config: DefaultGameConfig) {
  try {
    this.versionConfigFile.run {
      writeObjectToFile(config, prettyGsonBuilder)
    }
    logmaker.info("${this.versionNumber} $config")
  } catch (e: IOException) {
    logmaker.error(e.getExceptionMessage())
  }
}
