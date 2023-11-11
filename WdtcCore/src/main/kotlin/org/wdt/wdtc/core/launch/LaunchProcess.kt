package org.wdt.wdtc.core.launch

import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.utils.ThreadUtils.startThread
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

class LaunchProcess(private val process: Process) {
    var setUIText: TextInterface? = null
    private val logmaker = getLogger(LaunchProcess::class.java)

    @Throws(IOException::class)
    fun startLaunchGame() {
        try {
            startThread { getRunInfo(process.inputStream) }
            startThread { getRunInfo(process.errorStream) }.join()
            logmaker.info("Game Stop")
        } catch (e: InterruptedException) {
            logmaker.error("Run Command Error,", e)
        }
    }

    private fun getRunInfo(inputStream: InputStream) {
        try {
            val Reader = BufferedReader(InputStreamReader(inputStream, "GBK"))
            var line: String?
            while (Reader.readLine().also { line = it } != null) {
                if (Thread.currentThread().isInterrupted) {
                    launchErrorTask()
                    return
                } else {
                    val ErrorWarn = Pattern.compile("FATAL").matcher(line)
                    if (ErrorWarn.find()) {
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
