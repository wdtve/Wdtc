package org.wdt.wdtc.core.launch

import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.utils.ThreadUtils.startThread
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

class LaunchProcess(private val process: Process) {
  var setUIText: TextInterface? = null
  private val logmaker = LaunchProcess::class.java.getWdtcLogger()

  @Throws(IOException::class)
  fun startLaunchGame() {
    try {
      Runnable { getRunInfo(process.inputStream) }.startThread()
      Runnable { getRunInfo(process.errorStream) }.startThread().join()
      logmaker.info("Game stop")
    } catch (e: InterruptedException) {
      logmaker.error("Run command error,", e)
    }
  }

  private fun getRunInfo(inputStream: InputStream) {
    try {
      val reader = BufferedReader(InputStreamReader(inputStream, "GBK"))
      var line: String?
      while (reader.readLine().also { line = it } != null) {
        if (Thread.currentThread().isInterrupted) {
          launchErrorTask()
          return
        } else {
          val errorWarn = Pattern.compile("FATAL").matcher(line)
          if (errorWarn.find()) {
            println(line)
            Thread.currentThread().interrupt()
          } else {
            println(line)
          }
        }
      }
    } catch (e: IOException) {
      logmaker.error("Run Command Error,", e)
    }
  }

  // TODO Optimize error display
  @Throws(IOException::class)
  private fun launchErrorTask() {
    setUIText!!.setControl("启动失败:${IOUtils.toString(process.errorStream)}${IOUtils.toString(process.inputStream)}")
  }

}
