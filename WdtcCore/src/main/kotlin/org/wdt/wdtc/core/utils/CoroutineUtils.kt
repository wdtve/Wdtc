@file:JvmName("CoroutineUtils") @file:Suppress("NOTHING_TO_INLINE")

package org.wdt.wdtc.core.utils

import kotlinx.coroutines.*

suspend inline fun suspendKeepRun(timeMillis: Long = 0, block: () -> Unit) {
  while (true) {
    block()
    delay(timeMillis)
  }
}

@OptIn(DelicateCoroutinesApi::class)
inline fun newCoroutineWithName(name: String, noinline block: suspend CoroutineScope.() -> Unit): Job {
  return GlobalScope.launch(name.toCoroutineName(), block = block)
}


inline val ioCoroutineScope
  get() = CoroutineScope(Dispatchers.IO)

inline fun String.toCoroutineName(): CoroutineName = CoroutineName(this)

@OptIn(DelicateCoroutinesApi::class)
fun executorCoroutineScope(thread: Int = 64, name: String): CoroutineScope {
  return CoroutineScope(newFixedThreadPoolContext(thread, name))
}