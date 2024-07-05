package org.wdt.wdtc.core.impl.manger

import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.game.currentVersionsList
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionsListGson

private operator fun VersionsList.plus(versionsList: VersionsList): VersionsList {
	return apply {
		addAll(versionsList.mapTo(VersionsList()) {
			it.gameConfig.configVersion
		})
	}
}


fun ckeckVersionsList() {
	removeInvalidVersions()
	val downloadedVersions = GameDirectoryManager.DEFAULT_GAME_DIRECTORY.currentDownlaodedVersionList
	versionsJson.writeObjectToFile(serializeVersionsListGson) {
		currentVersionsList + downloadedVersions
	}
}

fun removeInvalidVersions() {
	versionsJson.writeObjectToFile(serializeVersionsListGson) {
		currentVersionsList.filterTo(VersionsList()) {
			it.ckeckIsEffective()
		}
	}
}