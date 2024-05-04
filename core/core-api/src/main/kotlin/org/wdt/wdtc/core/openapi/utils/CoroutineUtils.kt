@file:JvmName("CoroutineUtils")

package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val ioCoroutineScope = coroutineScope(Dispatchers.IO)
val defaultCoroutineScope = coroutineScope(Dispatchers.Default)

fun launchScope(
	name: String = "New coroutine",
	coroutineContext: CoroutineContext = EmptyCoroutineContext,
	block: suspend CoroutineScope.() -> Unit
): Job {
	return defaultCoroutineScope.launch(name.toCoroutineName() + coroutineContext, block = block)
}

fun <T> ioAsync(block: suspend CoroutineScope.() -> T): Deferred<T> = ioCoroutineScope.async(block = block)

fun CoroutineScope.launch(name: String, block: suspend CoroutineScope.() -> Unit): Job =
	launch(context = name.toCoroutineName(), block = block)

suspend fun <T> runOnIO(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)

fun String.toCoroutineName(): CoroutineName = CoroutineName(this)

@OptIn(DelicateCoroutinesApi::class)
fun executorCoroutineScope(thread: Int = 64, name: String): CoroutineScope {
	return coroutineScope(newFixedThreadPoolContext(thread, name))
}

fun coroutineScope(context: CoroutineContext): CoroutineScope = object : CoroutineScope {
	override val coroutineContext: CoroutineContext = context
}

inline fun timer(name: String? = null, period: Long, crossinline action: TimerTask.() -> Unit) {
	timer(name, true, Date.from(Instant.now()), period, action)
}

suspend fun Job.runBlocking() {
	start()
	return join()
}