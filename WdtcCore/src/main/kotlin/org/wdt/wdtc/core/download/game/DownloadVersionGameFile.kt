package org.wdt.wdtc.core.download.game

import kotlinx.coroutines.coroutineScope
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.createDirectories
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.download.game.GameVersionList.GameVersionsJsonObjectImpl
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.launch.GameRuntimeList
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.*

class DownloadVersionGameFile(val version: Version, private val install: Boolean) {
	
	fun startDownloadGameVersionJson() {
		if (!install && version.versionJson.isFileExists()) return
		if (isOfficialDownloadSource) {
			GameVersionList().versionList.forEach {
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
	
	fun startDownloadGameAssetsListJson() {
		val data = version.gameVersionJsonObject.assetIndex
		val listJsonURL = data.url.let {
			if (isOfficialDownloadSource) it
			else it.toString().replace(pistonMetaMojang, currentDownloadSource.metaUrl).toURL()
		}
		version.gameAssetsListJson.also {
			if (it.compareFile(data)) {
				startDownloadTask(listJsonURL to it)
			}
		}
	}
	
	fun startDownloadVersionJar() {
		val data = version.gameVersionJsonObject.downloads.client
		val jarUrl = data.run {
			if (isOfficialDownloadSource) url
			else currentDownloadSource.versionClientUrl.format(version.versionNumber, "client").toURL()
		}
		version.versionJar.let {
			if (it.compareFile(data)) {
				startDownloadTask(jarUrl to it)
			}
		}
	}
	
	suspend fun startDownloadLibraryFile(): Unit = coroutineScope {
		launch("Donwnload library file") {
			GameRuntimeList(version).runtimeList.run {
				val speed = ProgressManger(size).apply {
					coroutineScope = executorCoroutineScope(name = "Download library file")
				}
				forEach {
					DownloadGameRuntime(version, it, speed).run {
						start()
					}
				}
				speed.await()
			}
		}
		launch("Copy log4j file") {
			IOUtils.copy(getResourceAsStream("/assets/log4j2.xml"), version.versionLog4j2.outputStream())
		}
	}
	
	fun startDownloadAssetsFiles() = DownloadGameAssetsFile(version).run { startDownloadAssetsFiles() }
	
	
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
		fun startDownloadVersionManifestJsonFile() {
			startDownloadTask(currentDownloadSource.versionManifestUrl, versionManifestFile)
		}
	}
}
