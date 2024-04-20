package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import okio.Path.Companion.toOkioPath
import okio.buffer
import org.wdt.utils.io.okio.touch
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.system.measureTimeMillis

class DownloadUtils(
	url: URL,
	targetFile: File
) {
	
	@OptIn(ExperimentalCoroutinesApi::class)
	private val scope = CoroutineScope(Dispatchers.IO.limitedParallelism(2))
	
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
	pair().let {
		if (it.second compareFile fileData) {
			startDownloadTask(it)
		}
	}
}


@Throws(IOException::class)
suspend fun Pair<URL, File>.manyTimesToTryDownload(times: Int) {
	lateinit var exception: IOException
	repeat(times) {
		exception = try {
			DownloadUtils(first, second).run {
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

