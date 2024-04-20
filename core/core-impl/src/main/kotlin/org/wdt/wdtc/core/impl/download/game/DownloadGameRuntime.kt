package org.wdt.wdtc.core.impl.download.game

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.impl.launch.GameRuntimeData
import org.wdt.wdtc.core.impl.launch.GameRuntimeFile
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObject.Companion.nativesLibraryArtifact
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.ProgressManger
import org.wdt.wdtc.core.openapi.manger.TaskKind
import org.wdt.wdtc.core.openapi.manger.TaskManger
import org.wdt.wdtc.core.openapi.manger.finishCountDown
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File

class DownloadGameRuntime(version: Version) {
	
	private val data = GameRuntimeData(version)
	
	private fun GameRuntimeData.getDownloadTask(
		gameRuntimeFile: GameRuntimeFile,
		speed: ProgressManger
	): DownloadGameRuntimeTask = gameRuntimeFile.libraryObject.run {
		if (gameRuntimeFile.nativesLibrary) {
			nativesLibraryArtifact.noNull().let {
				DownloadGameRuntimeTask(it.changedNativesLibraryFile, it.apply { url = changedNativesLibraryUrl }, speed)
			}
		} else {
			DownloadGameRuntimeTask(changedLibraryFile, downloads.artifact.apply { url = changedLibraryUrl }, speed)
		}
	}
	
	fun start() {
		data.runtimeList.run {
			val speed = ProgressManger(size).apply {
				coroutineScope = executorCoroutineScope(name = "Download game runtime")
			}
			forEach {
				data.getDownloadTask(it, speed).start()
			}
			speed.await()
		}
	}
	
	
	class DownloadGameRuntimeTask(
		file: File, artifact: LibraryObject.Artifact, progress: ProgressManger
	) : TaskManger(file.name, TaskKind.COROUTINES) {
		
		init {
			coroutinesAction = progress.run {
				coroutineScope.launch(actionName.toCoroutineName(), CoroutineStart.LAZY) {
					finishCountDown(file compareFile artifact) {
						startDownloadTask(artifact.url to file)
					}
				}
			}
		}
		
		override fun start() {
			coroutinesAction.noNull().start()
		}
	}
}

