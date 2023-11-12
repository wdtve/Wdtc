package org.wdt.wdtc.core.utils

import org.apache.log4j.*
import org.wdt.wdtc.core.manger.FileManger
import org.wdt.wdtc.core.manger.VMManger
import java.io.PrintWriter
import java.io.StringWriter

object WdtcLogger {
  @JvmStatic
  fun <T> getLogger(clazz: Class<T>): Logger {
    val logmaker = Logger.getLogger(clazz.getName())
    logmaker.addAppender(fileAppender)
    if (VMManger.isUI) {
      logmaker.addAppender(consoleAppender)
    }
    return logmaker
  }

  private val fileAppender: RollingFileAppender
    get() {
      val fileAppender = RollingFileAppender()
      fileAppender.setFile("${FileManger.wdtcConfig}/logs/Wdtc.log")
      fileAppender.append = true
      fileAppender.layout = PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p]:%t * %m%n")
      fileAppender.setMaxFileSize("10MB")
      fileAppender.maxBackupIndex = 10
      fileAppender.threshold = Level.DEBUG
      fileAppender.activateOptions()
      return fileAppender
    }
  private val consoleAppender: ConsoleAppender
    get() {
      val consoleAppender = ConsoleAppender(PatternLayout("[%d{HH:mm:ss}] [%C.%M/%p] * %m%n"))
      consoleAppender.setTarget("System.err")
      consoleAppender.immediateFlush = true
      consoleAppender.encoding = "UTF-8"
      consoleAppender.threshold = if (VMManger.isDebug) Level.DEBUG else Level.INFO
      consoleAppender.activateOptions()
      return consoleAppender
    }

  @JvmStatic
  fun Throwable.getExceptionMessage(): String {
    val sw = StringWriter()
    this.printStackTrace(PrintWriter(sw, true))
    return sw.buffer.toString()
  }

  fun <T> Class<T>.getWdtcLogger(): Logger {
    return getLogger(this)
  }
}
