package org.wdt.wdtc.core.openapi.manger

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.openapi.game.GameConfig
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionGson
import org.wdt.wdtc.core.openapi.utils.logmaker

class GameConfigManger(private val version: Version) {
	val config: GameConfig.Config
		get() = configFileObject.config
	
	val configFileObject: GameConfig
		get() = version.versionConfigFile.readFileToClass(serializeVersionGson)
	val configVersion: Version
		get() = configFileObject.version
	
	fun putConfigToFile(config: GameConfig) {
		version.writeConfigJsonToFile(config)
	}
}

fun writeConfigJsonToAllVersion() {
	GameDirectoryManger.DEFAULT_GAME_DIRECTORY.currentDownlaodedVersionList.run {
		if (isNotEmpty()) {
			forEach {
				if (it.versionConfigFile.isFileNotExists()) {
					it.writeConfigJson()
				}
			}
		} else {
			logmaker.warning("There are currently no game versions available")
		}
	}
}

fun Version.writeConfigJson() = writeConfigJsonToFile(GameConfig(this))

val Version.gameConfig: GameConfigManger
	get() = GameConfigManger(this)

private fun Version.writeConfigJsonToFile(config: GameConfig) {
	versionConfigFile.writeObjectToFile(serializeVersionGson) {
		config.also {
			logmaker.info("${this.versionNumber} $it")
		}
	}
}
