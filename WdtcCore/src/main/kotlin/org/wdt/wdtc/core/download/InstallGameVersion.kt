package org.wdt.wdtc.core.download

import kotlinx.coroutines.coroutineScope
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.VersionsList.Companion.changeListToFile
import org.wdt.wdtc.core.game.currentVersionsList
import org.wdt.wdtc.core.manger.writeConfigJson
import org.wdt.wdtc.core.utils.launch
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.modInstallTask

// TODO Optimize download speed
class InstallGameVersion(
	private val version: Version, private val install: Boolean = false,
	private val printInfo: (suspend (String) -> Unit)? = null
) {
	
	private val download: DownloadVersionGameFile = DownloadVersionGameFile(version, install)
	
	
	suspend fun startInstallGame(): Unit = coroutineScope {
		val startTime = System.currentTimeMillis()
		val task = version.modInstallTask
		
		download.createGameDirectories()
		
		launch("Write and ckeck config file") {
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
		
		if (install) task?.run {
			beforInstallTask()
			writeVersionJsonPatches()
			overwriteVersionJson()
		}
		
		download.startDownloadLibraryFile()
		
		System.currentTimeMillis().minus(startTime).also {
			printInfo?.invoke("下载游戏所需类库完成,耗时${it}ms")
			logmaker.info("Download game runtime finish,take a period of: ${it}ms")
		}
		
		if (install) task?.afterDownloadTask()
		
		download.startDownloadAssetsFiles()
		
		System.currentTimeMillis().minus(startTime).also {
			printInfo?.invoke("游戏下载完成,耗时${it}ms")
			logmaker.info("Download game finish,take a period of: ${it}ms")
		}
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

