package org.wdt.wdtc.core.impl.download.game

import kotlinx.coroutines.coroutineScope
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.isFileOlder
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.game.GameVersionJsonObject
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.*
import java.util.*

class DownloadVersionGameFile(private val version: Version, private val install: Boolean) {
	
	private val versionJson: GameVersionJsonObject by lazy {
		version.gameVersionJsonObject
	}
	
	suspend fun startDownloadGameVersionJson() {
		if (!install && version.versionJson.isFileExists()) return
		if (isOfficialDownloadSource) {
			GameVersionList().getVersionList().toList().also { versions ->
				if (versions.none { it.versionNumber == this.version.versionNumber }) {
					throw VersionNotFoundException("${version.versionNumber} not found")
				}
			}.map {
				it as GameVersionList.GameVersionsJsonObjectImpl
			}.filter {
				it.versionNumber == version.versionNumber
			}.component1().let {
				startDownloadTask(it.versionJsonURL to version.versionJson)
			}
		} else {
			currentDownloadSource.versionClientURL.format(version.versionNumber, "json").toURL().also {
				startDownloadTask(it to version.versionJson)
			}
		}
	}
	
	suspend fun startDownloadGameAssetsListJson() {
		val data = versionJson.assetIndex
		val listJsonURL = data.url.let {
			if (isOfficialDownloadSource) it
			else it.toString().replace(pistonMetaMojang, currentDownloadSource.metaURL).toURL()
		}
		createDownloadTask(data) {
			listJsonURL to version.gameAssetsListJson
		}.doExecutor()
	}
	
	suspend fun startDownloadVersionJar() {
		val data = versionJson.downloads.client
		val jarUrl = data.run {
			if (isOfficialDownloadSource) url
			else currentDownloadSource.versionClientURL.format(version.versionNumber, "client").toURL()
		}
		createDownloadTask(data) {
			jarUrl to version.versionJar
		}.doExecutor()
	}
	
	suspend fun startDownloadLibraryFile(): Unit = coroutineScope {
		launch("Donwnload library file") { doExecutor(DownloadGameRuntime(version)) }
		launch("Copy log4j file") {
			(getResourceAsStream("/assets/log4j2.xml") to version.versionLog4j2.outputStream()).copyTo()
		}
	}
	
	suspend fun startDownloadAssetsFiles() = doExecutor(DownloadGameAssetsFile(version))
	
	
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
			startDownloadTask(currentDownloadSource.versionManifestURL.toURL() to versionManifestFile)
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
