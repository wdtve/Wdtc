package org.wdt.wdtc.core.utils

import kotlinx.coroutines.*
import okio.Path.Companion.toOkioPath
import okio.buffer
import org.wdt.utils.io.okio.touch
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.system.measureTimeMillis

class DownloadUtils(private val url: URL, targetFile: File) {
	
	@OptIn(ExperimentalCoroutinesApi::class)
	private val downloadCoroutineSocpe = CoroutineScope(Dispatchers.IO.limitedParallelism(2))
	
	private val targetFileSink = targetFile.toOkioPath(true).let {
		downloadCoroutineSocpe.async {
			runOnIO {
				if (!fileSystem.exists(it)) {
					targetFile.touch()
				}
			}
			fileSystem.sink(it).buffer()
		}
	}
	private val urlSource = downloadCoroutineSocpe.async {
		url.newInputStream().toBufferedSource()
	}
	
	
	suspend fun startDownloadFile() {
		targetFileSink.await().use { sink ->
			urlSource.await().use { source ->
				sink.write(source.readByteArray())
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

suspend fun startDownloadTask(pair: Pair<URL, File>) {
	pair.run {
		logmaker.info("Task Start: $first")
		measureTimeMillis { manyTimesToTryDownload(5) }.also {
			logmaker.info("Task Finish: $second, Take A Period Of $it ms")
		}
	}
}


suspend fun Pair<URL, File>.manyTimesToTryDownload(times: Int) {
	lateinit var exception: IOException
	repeat(times) {
		exception = try {
			DownloadUtils(first, second).run {
				startDownloadFile()
			}
			return
		} catch (e: IOException) {
			logmaker.warning(e.message)
			e
		}
		delay(500)
	}
	throw exception
}


