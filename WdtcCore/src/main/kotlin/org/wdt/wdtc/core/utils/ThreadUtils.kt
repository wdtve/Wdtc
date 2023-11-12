package org.wdt.wdtc.core.utils

object ThreadUtils {
  @JvmStatic
  fun Runnable.startThread(): Thread {
    val thread = Thread(this)
    thread.start()
    return thread
  }
}
