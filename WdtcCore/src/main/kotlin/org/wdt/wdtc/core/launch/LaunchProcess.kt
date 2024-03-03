package org.wdt.wdtc.core.launch

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.utils.error
import org.wdt.wdtc.core.utils.logmaker
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

class LaunchProcess(
  private val process: ProcessBuilder,
  var printInfo: (String) -> Unit
) {
  private val builder = StringBuilder()
  fun startLaunchGame() {
    try {
      process.start().run {
        val runtime = measureTimeMillis {
          runBlocking(Dispatchers.IO) {
            launch(CoroutineName("Read info inputStream")) { getRunInfo(inputStream) }
            launch(CoroutineName("Read error inputStream")) { getRunInfo(errorStream) }
          }
        }
        logmaker.info("Game over. Game run time: $runtime ms")
      }
    } catch (e: InterruptedException) {
      logmaker.error("Run command error,", e)
    }
  }

  private fun getRunInfo(inputStream: InputStream) {
    try {
      val thread = Thread.currentThread()
      val reader = inputStream.reader(Charset.forName("GBK")).buffered()
      var line: String
      while (reader.readLine().also { line = it } != null) {
        line.let {
          if (thread.isInterrupted) {
            launchErrorTask()
            return
          } else {
            if (Pattern.compile("FATAL").matcher(it).find()) {
              println(it)
              builder.append(it)
              thread.interrupt()
            } else {
              println(it)
              builder.append(it)
            }
          }
        }
      }
    } catch (e: IOException) {
      logmaker.error("Run Command Error,", e)
    }
  }

  private fun launchErrorTask() {
    printInfo("启动失败:\n${builder}")
  }
}
