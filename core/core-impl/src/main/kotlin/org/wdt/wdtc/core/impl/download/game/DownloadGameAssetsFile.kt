package org.wdt.wdtc.core.impl.download.game

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.ProgressManger
import org.wdt.wdtc.core.openapi.manger.TaskKind
import org.wdt.wdtc.core.openapi.manger.TaskManger
import org.wdt.wdtc.core.openapi.manger.currentDownloadSource
import org.wdt.wdtc.core.openapi.utils.*

class DownloadGameAssetsFile(private val version: Version) {
	
	suspend fun startDownloadAssetsFiles() {
		val assets = runOnIO {
			version.gameAssetsListJson
		}.readFileToJsonObject().getJsonObject("objects").asMap().values
		
		val progress = ProgressManger(assets.size).apply {
			coroutineScope = executorCoroutineScope(name = "Download assets files")
		}
		
		assets.asSequence().map {
			it.asJsonObject.parseObject<AssetsFileData>()
		}.forEach { data ->
			DownloadGameAssetsFileTask(version, data, progress).run {
				start()
			}
		}
		progress.await()
	}
	
	class AssetsFileData(
		@SerializedName("hash")
		override val sha1: String,
		@SerializedName("size")
		override val size: Long = 0
	) : FileData {
		val hashSplicing: String
			get() = "$hashHead/$sha1"
		private val hashHead: String
			get() = sha1.substring(0, 2)
	}
	
	private class DownloadGameAssetsFileTask(
		version: Version, data: AssetsFileData, progress: ProgressManger
	) : TaskManger(data.sha1, TaskKind.COROUTINES) {
		
		
		init {
			val hashFile = version.gameAssetsObjectsDirectory.resolve(data.hashSplicing)
			coroutinesAction = progress.run {
				coroutineScope.launch(actionName.toCoroutineName(), CoroutineStart.LAZY) {
					downloadFinishCountDown(data) {
						currentDownloadSource.assetsUrl.plus(data.hashSplicing).toURL() to hashFile
					}
				}
			}
		}
		
		override fun start() {
			coroutinesAction.noNull().start()
		}
	}
}
