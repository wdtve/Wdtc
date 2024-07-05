package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import okio.BufferedSink
import okio.BufferedSource
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.use
import org.wdt.utils.io.okio.toBufferedSource
import org.wdt.wdtc.core.openapi.manager.*
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

open class DownloadTask(
	val url: URL, val targetFile: File
) : AbstractTask(targetFile.name, TaskKind.COROUTINE), TaskExecutor, RunTaskExecutor, Closeable {
	
	init {
		if (!targetFile.exists()) {
			Files.createDirectories(targetFile.toPath().parent)
			Files.createFile(targetFile.toPath())
		}
	}
	
	constructor(pair: Pair<URL, File>) : this(pair.first, pair.second)
	
	private lateinit var pair: Pair<BufferedSource, BufferedSink>
	
	private var spend: Long = 0
	
	var isNecessary: Boolean = true
	
	
	override suspend fun start() {
		pair.run {
			second.write(first.readByteString())
		}
	}
	
	@Throws(IOException::class)
	private fun initStream() {
		pair = url.newInputStream().toBufferedSource() to targetFile.let {
			fileSystem.sink(it.toOkioPath(true)).buffer()
		}
	}
	
	override fun before() {
		logmaker.info("Task Start: $url")
		initStream()
	}
	
	override suspend fun run(task: AbstractTask) {
		spend = measureTimeMillis { task.start() }
	}
	
	override fun after() {
		logmaker.info("Task Finish: ${targetFile.canonicalPath}, Take A Period Of $spend ms")
	}
	
	@Throws(IOException::class)
	override suspend fun doExecutor() {
		if (!isNecessary) return
		val count = AtomicInteger()
		val list = generateSequence {
			runCatching {
				count.getAndIncrement()
				use {
					before()
					runBlocking { run(it) }
					after()
				}
			}.takeUnless {
				it.isSuccess || count.get() == 5
			}
		}.toList()
		
		list.filter { it.isFailure }.also { results ->
			if (results.isNotEmpty()) results.first().exceptionOrNull()?.let {
				throw DownloadException(this, Thread.currentThread(), it)
			}
		}
	}
	
	override fun close() {
		pair.toList().forEach { it.close() }
	}
	
	override fun toString(): String {
		return "DownloadTask(targetFile=$targetFile, url=$url)"
	}
	
}

class DownloadException(
	task: DownloadTask,
	thread: Thread,
	cause: Throwable
) : IOException(buildString {
	append("Download task exception:$task on ${thread.name}")
}, cause)

suspend fun startDownloadTask(url: String, file: File) {
	startDownloadTask(url.toURL() to file)
}


suspend fun startDownloadTask(url: URL, file: File) {
	startDownloadTask(url to file)
}

@Throws(IOException::class)
suspend fun startDownloadTask(pair: Pair<URL, File>) {
	pair.run {
		doExecutor(DownloadTask(first, second))
	}
}

@Throws(IOException::class)
fun createDownloadTask(fileData: FileData, pair: () -> Pair<URL, File>): DownloadTask = DownloadTask(pair()).apply {
	isNecessary = targetFile compare fileData
}