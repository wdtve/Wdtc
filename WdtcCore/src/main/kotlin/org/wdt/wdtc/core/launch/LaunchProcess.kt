package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.utils.logmaker
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern
import kotlin.concurrent.thread

class LaunchProcess(
  private val process: Process,
  var printInfo: (String) -> Unit
) {
  private val builder = StringBuilder()
  fun startLaunchGame() {
    try {
      thread(name = "Read info inputStream") { getRunInfo(process.inputStream) }
      thread(name = "Read error inputStream") { getRunInfo(process.errorStream) }.join()
      logmaker.info("Game stop")
    } catch (e: InterruptedException) {
      logmaker.error("Run command error,", e)
    }
  }

  private fun getRunInfo(inputStream: InputStream) {
    try {
      val thread = Thread.currentThread()
      val reader = BufferedReader(InputStreamReader(inputStream, "GBK"))
      var line: String
      while (reader.readLine().also { line = it } != null) {
        if (thread.isInterrupted) {
          launchErrorTask()
          return
        } else {
          val errorWarn = Pattern.compile("FATAL").matcher(line)
          if (errorWarn.find()) {
            println(line)
            builder.append(line)
            thread.interrupt()
          } else {
            println(line)
            builder.append(line)
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
