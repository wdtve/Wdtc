package org.wdt.wdtc.core.utils

import okio.Path
import okio.Path.Companion.toOkioPath
import okio.buffer
import org.wdt.utils.io.okio.touch
import org.wdt.utils.io.toFile
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.system.measureTimeMillis

class DownloadUtils(targetFile: File, private val url: URL) {
  private val targetPath: Path = targetFile.toOkioPath()

  fun manyTimesToTryDownload(times: Int) {
    lateinit var exception: IOException
    repeat(times) {
      exception = try {
        startDownloadFile()
        return
      } catch (e: IOException) {
        e
      }
    }
    throw exception
  }

  private fun startDownloadFile() {
    if (!fileSystem.exists(targetPath)) {
      targetPath.toFile().touch()
    }
    fileSystem.sink(targetPath).buffer().use { sink ->
      url.newInputStream().toBufferedSource().use { source ->
        sink.write(source.readByteArray())
      }
    }
  }

}

fun startDownloadTask(url: String, path: String) {
  startDownloadTask(url.toURL(), path.toFile())
}

fun startDownloadTask(url: String, file: File) {
  startDownloadTask(url.toURL(), file)
}

fun startDownloadTask(url: URL, file: File) {
  val downloadUtils = DownloadUtils(file, url)
  try {
    logmaker.info("Task Start: $url")
    val spentTime = measureTimeMillis { downloadUtils.manyTimesToTryDownload(5) }
    logmaker.info("Task Finish: $file, Take A Period Of $spentTime ms")
  } catch (e: Exception) {
    logmaker.error("Task: $url Error", e)
  }
}

