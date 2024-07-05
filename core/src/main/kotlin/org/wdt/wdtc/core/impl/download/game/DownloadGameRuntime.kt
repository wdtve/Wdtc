package org.wdt.wdtc.core.impl.download.game

import org.wdt.wdtc.core.impl.launch.GameRuntimeData
import org.wdt.wdtc.core.impl.launch.GameRuntimeFile
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObject.Companion.nativesLibraryArtifact
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.MultithreadingManager
import org.wdt.wdtc.core.openapi.manager.RunTaskExecutor
import org.wdt.wdtc.core.openapi.utils.DownloadTask
import org.wdt.wdtc.core.openapi.utils.compare
import org.wdt.wdtc.core.openapi.utils.newThreadPoolContext
import org.wdt.wdtc.core.openapi.utils.noNull
import java.io.File

class DownloadGameRuntime(version: Version) : RunTaskExecutor {
	
	private val data = GameRuntimeData(version)
	
	private fun GameRuntimeData.createDownloadTask(
		file: GameRuntimeFile
	): GameRuntimeDownloadTask = file.library.run {
		if (file.isNatives) {
			nativesLibraryArtifact.noNull().let {
				GameRuntimeDownloadTask(it.changedNativesLibraryFile, it.apply { url = changedNativesLibraryURL })
			}
		} else {
			GameRuntimeDownloadTask(changedLibraryFile, downloads.artifact.apply { url = changedLibraryURL })
		}
	}
	
	override suspend fun doExecutor() {
		data.runtimeList.let {
			MultithreadingManager(it.size).apply {
				context = newThreadPoolContext(name = "Download library files")
			}.doExecuters {
				it.map { file -> data.createDownloadTask(file) }
			}
		}
	}
	
	class GameRuntimeDownloadTask(
		file: File, artifact: LibraryObject.Artifact
	) : DownloadTask(artifact.url to file) {
		init {
			isNecessary = file compare artifact
		}
	}
}

