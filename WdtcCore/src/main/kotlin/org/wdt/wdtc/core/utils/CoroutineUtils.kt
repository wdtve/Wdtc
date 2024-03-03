@file:JvmName("CoroutineUtils")

package org.wdt.wdtc.core.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun scwn(
	name: String = "New coroutine",
	coroutineContext: CoroutineContext = EmptyCoroutineContext,
	block: suspend CoroutineScope.() -> Unit
): Job {
	return defaultCoroutineScope.launch(name.toCoroutineName() + coroutineContext, block = block)
}

fun CoroutineScope.launch(name: String, block: suspend CoroutineScope.() -> Unit) =
	launch(context = name.toCoroutineName(), block = block)


val ioCoroutineScope = CoroutineScope(Dispatchers.IO)
val defaultCoroutineScope = CoroutineScope(Dispatchers.Default)

fun String.toCoroutineName(): CoroutineName = CoroutineName(this)

@OptIn(DelicateCoroutinesApi::class)
fun executorCoroutineScope(thread: Int = 64, name: String): CoroutineScope {
	return CoroutineScope(newFixedThreadPoolContext(thread, name))
}