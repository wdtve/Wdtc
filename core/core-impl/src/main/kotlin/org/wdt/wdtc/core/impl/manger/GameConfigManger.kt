package org.wdt.wdtc.core.impl.manger

import org.wdt.wdtc.core.openapi.game.VersionsList.Companion.changeListToFile
import org.wdt.wdtc.core.openapi.game.currentVersionsList
import org.wdt.wdtc.core.openapi.manger.GameDirectoryManger
import org.wdt.wdtc.core.openapi.manger.currentDownlaodedVersionList
import org.wdt.wdtc.core.openapi.manger.gameConfig
import org.wdt.wdtc.core.openapi.utils.logmaker

fun ckeckVersionsList() {
	GameDirectoryManger.DEFAULT_GAME_DIRECTORY.currentDownlaodedVersionList.forEach {
		if (it !in currentVersionsList) {
			currentVersionsList.changeListToFile {
				it.gameConfig.configVersion.addToList()
			}
		}
	}
	removeInvalidVersions()
}

fun removeInvalidVersions() {
	currentVersionsList.run {
		try {
			forEach {
				if (!it.ckeckIsEffective()) {
					logmaker.info("$it invalid")
					changeListToFile { remove(it) }
				}
			}
		} catch (_: ConcurrentModificationException) {
			removeInvalidVersions()
		}
	}
	
}
