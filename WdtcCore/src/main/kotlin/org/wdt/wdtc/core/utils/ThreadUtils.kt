@file:JvmName("ThreadUtils")

package org.wdt.wdtc.core.utils

fun Runnable.startThread(): Thread {
  val thread = Thread(this)
  thread.start()
  return thread
}
