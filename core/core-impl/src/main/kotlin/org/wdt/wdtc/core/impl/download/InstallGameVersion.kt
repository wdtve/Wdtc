package org.wdt.wdtc.core.impl.download

import kotlinx.coroutines.coroutineScope
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.impl.manger.removeInvalidVersions
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.VersionsList.Companion.changeListToFile
import org.wdt.wdtc.core.openapi.game.currentVersionsList
import org.wdt.wdtc.core.openapi.manger.modInstallTask
import org.wdt.wdtc.core.openapi.manger.writeConfigJson
import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.runOnIO

// TODO Optimize download speed
class InstallGameVersion(
	private val version: Version,
	private val install: Boolean = false,
	private val printInfo: (suspend (String) -> Unit)? = null
) {
	
	private val download: DownloadVersionGameFile = DownloadVersionGameFile(version, install)
	
	
	suspend fun startInstallGame(): Unit = coroutineScope {
		val startTime = System.currentTimeMillis()
		
		download.createGameDirectories()
		
		val job = launch("Write and ckeck config file") {
			if (install) {
				version.writeConfigJson()
			}
		}
		
		launch("Add version to versions list") {
			currentVersionsList.changeListToFile {
				version.addToList()
			}
		}
		
		startDownloadGameFileTask()
		
		val task = runOnIO {
			job.join()
			version.modInstallTask
		}
		
		if (install) task?.run {
			beforInstallTask()
			writeVersionJsonPatches()
			overwriteVersionJson()
		}
		
		download.startDownloadLibraryFile()
		
		val libraryTime = System.currentTimeMillis().minus(startTime).also {
			printInfo?.invoke("下载游戏所需类库完成,耗时${it}ms")
		}
		logmaker.info("Download game runtime finish,take a period of: ${libraryTime}ms")
		
		
		if (install) task?.afterDownloadTask()
		
		download.startDownloadAssetsFiles()
		
		removeInvalidVersions()
		
		val fullTime = System.currentTimeMillis().minus(startTime).also {
			printInfo?.invoke("游戏下载完成,耗时${it}ms")
		}
		logmaker.info("Download game finish,take a period of: ${fullTime}ms")
	}
	
	private suspend fun startDownloadGameFileTask() {
		coroutineScope {
			download.run {
				startDownloadGameVersionJson()
				launch("Download assets list json") { startDownloadGameAssetsListJson() }
				launch("Download version jar") { startDownloadVersionJar() }
			}
		}
	}
}

