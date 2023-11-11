package org.wdt.wdtc.core.utils

object ThreadUtils {
	@JvmStatic
	fun startThread(runnable: Runnable?): Thread {
		val thread = Thread(runnable)
		thread.start()
		return thread
	}
}
