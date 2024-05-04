package org.wdt.wdtc.core.impl.download.game

import kotlinx.coroutines.coroutineScope
import org.wdt.utils.io.*
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.game.GameVersionJsonObject
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.currentDownloadSource
import org.wdt.wdtc.core.openapi.manger.isOfficialDownloadSource
import org.wdt.wdtc.core.openapi.manger.pistonMetaMojang
import org.wdt.wdtc.core.openapi.manger.versionManifestFile
import org.wdt.wdtc.core.openapi.utils.*
import java.util.*

class DownloadVersionGameFile(private val version: Version, private val install: Boolean) {
	
	private val versionJson: GameVersionJsonObject by lazy {
		version.gameVersionJsonObject
	}
	
	suspend fun startDownloadGameVersionJson() {
		if (!install && version.versionJson.isFileExists()) return
		if (isOfficialDownloadSource) {
			GameVersionList().getVersionList().forEach {
				if (it is GameVersionList.GameVersionsJsonObjectImpl) {
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
		val data = versionJson.assetIndex
		val listJsonURL = data.url.let {
			if (isOfficialDownloadSource) it
			else it.toString().replace(pistonMetaMojang, currentDownloadSource.metaUrl).toURL()
		}
		startDownloadTask(data) {
			listJsonURL to version.gameAssetsListJson
		}
	}
	
	suspend fun startDownloadVersionJar() {
		val data = versionJson.downloads.client
		val jarUrl = data.run {
			if (isOfficialDownloadSource) url
			else currentDownloadSource.versionClientUrl.format(version.versionNumber, "client").toURL()
		}
		startDownloadTask(data) {
			jarUrl to version.versionJar
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
			gameAssetsObjectsDirectory.createDirectories()
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
			startDownloadTask(currentDownloadSource.versionManifestUrl.toURL() to versionManifestFile)
		}
		
		suspend fun downloadVersionManifestJsonFileTask() {
			val calendar = Calendar.getInstance().apply {
				time = Date()
				add(Calendar.DATE, -7)
			}
			versionManifestFile.run {
				if (isFileNotExists() || isFileOlder(calendar.time)) {
					startDownloadVersionManifestJsonFile()
				}
			}
		}
	}
}
