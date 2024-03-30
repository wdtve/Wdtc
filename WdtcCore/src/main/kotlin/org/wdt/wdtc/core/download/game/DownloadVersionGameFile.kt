package org.wdt.wdtc.core.download.game

import kotlinx.coroutines.coroutineScope
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionsJsonObjectImpl
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.currentDownloadSource
import org.wdt.wdtc.core.manger.isOfficialDownloadSource
import org.wdt.wdtc.core.manger.pistonMetaMojang
import org.wdt.wdtc.core.manger.versionManifestFile
import org.wdt.wdtc.core.utils.*

class DownloadVersionGameFile(val version: Version, private val install: Boolean) {
	
	suspend fun startDownloadGameVersionJson() {
		if (!install && version.versionJson.isFileExists()) return
		if (isOfficialDownloadSource) {
			val list = runOnIO {
				GameVersionList().versionList
			}
			list.forEach {
				if (it is GameVersionsJsonObjectImpl) {
					if (it.versionNumber == version.versionNumber) {
						startDownloadTask(it.versionJsonURL.noNull() to version.versionJson)
						return
					}
				}
			}
			throw VersionNotFoundException("${version.versionNumber} not found")
		} else {
			currentDownloadSource.versionClientUrl.format(version.versionNumber, "json").toURL().also {
				startDownloadTask(it to version.versionJson)
			}
		}
	}
	
	suspend fun startDownloadGameAssetsListJson() {
		val data = runOnIO {
			version.gameVersionJsonObject
		}.assetIndex
		val listJsonURL = data.url.let {
			if (isOfficialDownloadSource) it
			else it.toString().replace(pistonMetaMojang, currentDownloadSource.metaUrl).toURL()
		}
		version.gameAssetsListJson.also {
			if (it compareFile data) {
				startDownloadTask(listJsonURL to it)
			}
		}
	}
	
	suspend fun startDownloadVersionJar() {
		val data = runOnIO {
			version.gameVersionJsonObject
		}.downloads.client
		val jarUrl = data.run {
			if (isOfficialDownloadSource) url
			else currentDownloadSource.versionClientUrl.format(version.versionNumber, "client").toURL()
		}
		version.versionJar.let {
			if (it compareFile data) {
				startDownloadTask(jarUrl to it)
			}
		}
	}
	
	suspend fun startDownloadLibraryFile(): Unit = coroutineScope {
		launch("Donwnload library file") { DownloadGameRuntime(version).start() }
		launch("Copy log4j file") {
			IOUtils.copy(getResourceAsStream("/assets/log4j2.xml"), version.versionLog4j2.outputStream())
		}
	}
	
	suspend fun startDownloadAssetsFiles() = DownloadGameAssetsFile(version).run { startDownloadAssetsFiles() }
	
	
	fun createGameDirectories() {
		version.run {
			gameDirectory.createDirectories()
			gameLibraryDirectory.createDirectories()
			gameObjects.createDirectories()
			gameAssetsDirectory.createDirectories()
			gameVersionsDirectory.createDirectories()
			versionDirectory.createDirectories()
			versionNativesPath.createDirectories()
			gameModsPath.createDirectories()
			gameLogDirectory.createDirectories()
		}
	}
	
	companion object {
		suspend fun startDownloadVersionManifestJsonFile() {
			startDownloadTask(currentDownloadSource.versionManifestUrl, versionManifestFile)
		}
	}
}
