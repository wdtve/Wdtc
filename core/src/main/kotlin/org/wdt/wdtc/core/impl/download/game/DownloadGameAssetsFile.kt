package org.wdt.wdtc.core.impl.download.game

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.MultithreadingManager
import org.wdt.wdtc.core.openapi.manager.RunTaskExecutor
import org.wdt.wdtc.core.openapi.manager.currentDownloadSource
import org.wdt.wdtc.core.openapi.utils.*
import java.io.File
import java.io.IOException

class DownloadGameAssetsFile(private val version: Version) : RunTaskExecutor {
	
	override suspend fun doExecutor() {
		
		val assets = runOnIO {
			version.gameAssetsListJson
		}.readFileToJsonObject().getJsonObject("objects").asMap().values
		
		val progress = MultithreadingManager(assets.size).apply {
			context = newThreadPoolContext(64, "download asstes")
		}
		
		val failList = mutableListOf<AssetsDownloadTask>()
		
		progress.doExecuters {
			assets.map {
				it.asJsonObject.parseObject<AssetsFileData>()
			}.map { data ->
				AssetsDownloadTask(version.gameAssetsObjectsDirectory, data) { failList.add(it) }
			}
		}
		if (failList.isNotEmpty()) {
			MultithreadingManager(failList.size.also {
				logmaker.info("Number of fail task: $it")
			}).apply {
				context = newThreadPoolContext(32, "download asstes")
			}.doExecuters { failList }
		}
	}
	
	class AssetsFileData(
		@field:SerializedName("hash")
		override val sha1: String,
		@field:SerializedName("size")
		override val size: Long = 0
	) : FileData {
		val hashSplicing: String
			get() = "$hashHead/$sha1"
		private val hashHead: String
			get() = sha1.substring(0, 2)
	}
	
	class AssetsDownloadTask(
		directory: File,
		data: AssetsFileData,
		private val action: (AssetsDownloadTask) -> Unit
	) : DownloadTask(
		currentDownloadSource.assetsURL.plus(data.hashSplicing).toURL() to directory.resolve(data.hashSplicing)
	) {
		
		init {
			isNecessary = targetFile compare data
		}
		
		
		override suspend fun doExecutor() {
			try {
				super.doExecutor()
			} catch (_: IOException) {
				action(this)
			}
		}
	}
	
	
}
