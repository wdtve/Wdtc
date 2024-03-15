@file:JvmName("WdtcLogger") @file:Suppress("NOTHING_TO_INLINE")

package org.wdt.wdtc.core.utils

import org.wdt.utils.io.touch
import org.wdt.wdtc.core.manger.isUI
import org.wdt.wdtc.core.manger.wdtcConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.text.MessageFormat
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter
import kotlin.io.path.Path

private val FORMAT = MessageFormat("[{0,date,HH:mm:ss}] [{1}.{2}/{3}] {4}\n")

@field:JvmField
val logmaker: Logger = Logger.getLogger("Wdtc").apply {
	val logFile = Path(wdtcConfig.canonicalPath, "logs").resolve("Wdtc.log").also {
		it.touch()
	}
	level = Level.ALL
	useParentHandlers = false
	val formatter = object : Formatter() {
		override fun format(record: LogRecord): String {
			return formatLog(record)
		}
	}
	FileHandler(logFile.toString()).apply {
		level = Level.ALL
		this.formatter = formatter
		encoding = "UTF-8"
	}.let {
		addHandler(it)
	}
	ConsoleHandler().apply {
		this.formatter = formatter
		level = Level.ALL
		encoding = "UTF-8"
	}.let {
		if (isUI) {
			addHandler(it)
		}
	}
}


private fun formatLog(record: LogRecord): String {
	
	val thrown = record.thrown
	
	val writer: StringWriter?
	val buffer: StringBuffer
	if (thrown == null) {
		writer = null
		buffer = StringBuffer(256)
	} else {
		writer = StringWriter(1024)
		buffer = writer.buffer
	}
	
	FORMAT.format(
		arrayOf<Any>(
			Date(record.millis), record.sourceClassName, record.sourceMethodName, record.level.name, record.message
		), buffer, null
	)
	if (thrown != null) {
		writer?.let {
			PrintWriter(it).use { printWriter ->
				thrown.printStackTrace(printWriter)
			}
		}
	}
	return buffer.toString()
}


inline fun Logger.error(message: String) {
	this.log(Level.SEVERE, message)
}

inline fun Logger.error(e: Throwable) {
	this.log(Level.SEVERE, e.getExceptionMessage())
}

inline fun Logger.error(message: String, e: Throwable) {
	this.log(Level.SEVERE, message, e)
}

inline fun Logger.info(any: Any) {
	this.info(any.toString())
}

inline fun Logger.warning(message: String, e: Throwable) {
	this.log(Level.WARNING, message, e)
}


fun Throwable.getExceptionMessage(): String {
	return StringWriter().also {
		this.printStackTrace(PrintWriter(it, true))
	}.buffer.toString()
}


