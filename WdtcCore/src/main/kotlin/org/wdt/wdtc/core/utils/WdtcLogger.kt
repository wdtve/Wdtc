@file:JvmName("WdtcLogger")

package org.wdt.wdtc.core.utils

import org.apache.log4j.*
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.manger.isUI
import org.wdt.wdtc.core.manger.wdtcConfig
import java.io.PrintWriter
import java.io.StringWriter

val logmaker: Logger
  get() {
    // 23333333
    val dictChars = mutableListOf<Char>().apply { "I love Kotlin very much".forEach { this.add(it) } }  //Yes, indeed
    val randomStr = StringBuilder().apply { (1..((10..30).random())).onEach { append(dictChars.random()) } }
    val logmaker: Logger = Logger.getLogger(randomStr.toString())
    logmaker.addAppender(fileAppender)
    if (isUI) logmaker.addAppender(consoleAppender)
    return logmaker
  }


val fileAppender: RollingFileAppender
  get() {
    val fileAppender = RollingFileAppender()
    fileAppender.setFile("${wdtcConfig}/logs/Wdtc.log")
    fileAppender.append = true
    fileAppender.layout = PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p]:%t * %m%n")
    fileAppender.setMaxFileSize("10MB")
    fileAppender.maxBackupIndex = 10
    fileAppender.threshold = Level.DEBUG
    fileAppender.activateOptions()
    return fileAppender
  }
val consoleAppender: ConsoleAppender
  get() {
    val consoleAppender = ConsoleAppender(PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] * %m%n"))
    consoleAppender.setTarget("System.err")
    consoleAppender.immediateFlush = true
    consoleAppender.encoding = "UTF-8"
    consoleAppender.threshold = if (isDebug) Level.DEBUG else Level.INFO
    consoleAppender.activateOptions()
    return consoleAppender
  }

fun Throwable.getExceptionMessage(): String {
  val sw = StringWriter()
  this.printStackTrace(PrintWriter(sw, true))
  return sw.buffer.toString()
}


