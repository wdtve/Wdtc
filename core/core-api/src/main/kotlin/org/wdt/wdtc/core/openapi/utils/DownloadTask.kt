package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import okio.Path.Companion.toOkioPath
import okio.buffer
import org.wdt.utils.io.okio.touch
import org.wdt.wdtc.core.openapi.manger.ProgressManger
import org.wdt.wdtc.core.openapi.manger.TaskManger
import org.wdt.wdtc.core.openapi.manger.finishCountDown
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.system.measureTimeMillis

class DownloadTask(
	url: URL, targetFile: File
) : TaskManger(targetFile.name) {
	
	@OptIn(ExperimentalCoroutinesApi::class)
	private val scope = coroutineScope(Dispatchers.IO.limitedParallelism(2))
	
	private val targetFileSink = targetFile.toOkioPath(true).let {
		scope.async {
			runOnIO {
				if (!fileSystem.exists(it)) {
					targetFile.touch()
				}
			}
			fileSystem.sink(it).buffer()
		}
	}
	private val urlSource = scope.async {
		url.newInputStream().toBufferedSource()
	}
	
	override fun start() {
		logmaker.warning("Please run startDownloadFile()")
		runBlocking { startDownloadFile() }
	}
	
	suspend fun startDownloadFile() {
		targetFileSink.await().use { sink ->
			urlSource.await().use { source ->
				sink.write(source.readByteString())
			}
		}
	}
}

suspend fun startDownloadTask(url: String, file: File) {
	startDownloadTask(url.toURL() to file)
}


suspend fun startDownloadTask(url: URL, file: File) {
	startDownloadTask(url to file)
}

@Throws(IOException::class)
suspend fun startDownloadTask(pair: Pair<URL, File>) {
	pair.run {
		logmaker.info("Task Start: $first")
		measureTimeMillis { manyTimesToTryDownload(5) }.also {
			logmaker.info("Task Finish: $second, Take A Period Of $it ms")
		}
	}
}

@Throws(IOException::class)
suspend inline fun startDownloadTask(fileData: FileData, pair: () -> Pair<URL, File>) {
	pair().runWhen({ second compareFile fileData }) {
		startDownloadTask(this)
	}
}

@Throws(IOException::class)
suspend inline fun ProgressManger.downloadFinishCountDown(fileData: FileData, pair: () -> Pair<URL, File>) {
	pair().let {
		finishCountDown(it.second compareFile fileData) {
			startDownloadTask(it)
		}
	}
}


@Throws(IOException::class)
suspend fun Pair<URL, File>.manyTimesToTryDownload(times: Int) {
	lateinit var exception: IOException
	repeat(times) {
		exception = try {
			DownloadTask(first, second).run {
				startDownloadFile()
			}
			return
		} catch (e: IOException) {
			e
		}
		delay(500)
	}
	throw exception
}

