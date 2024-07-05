@file:JvmName("CoroutineUtils")

package org.wdt.wdtc.core.openapi.utils

import kotlinx.coroutines.*
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val ioScope = coroutineScope(Dispatchers.IO)

fun CoroutineContext.launch(
	context: CoroutineContext = EmptyCoroutineContext,
	block: suspend CoroutineScope.() -> Unit
): Job {
	return coroutineScope(this).launch(context, block = block)
}

fun <T> ioAsync(block: suspend CoroutineScope.() -> T): Deferred<T> = ioScope.async(block = block)

fun CoroutineScope.launch(name: String, block: suspend CoroutineScope.() -> Unit): Job =
	launch(context = name.toCoroutineName(), block = block)

fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
	return Dispatchers.IO.launch(block = block)
}

suspend fun <T> runOnIO(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)

fun String.toCoroutineName(): CoroutineName = CoroutineName(this)

@OptIn(DelicateCoroutinesApi::class)
fun newThreadPoolContext(thread: Int = 64, name: String? = null): CoroutineContext {
	return newFixedThreadPoolContext(thread, name ?: "New pool")
}

fun coroutineScope(context: CoroutineContext): CoroutineScope = object : CoroutineScope {
	override val coroutineContext: CoroutineContext = context
}

inline fun timer(name: String? = null, period: Long, crossinline action: TimerTask.() -> Unit) {
	timer(name, true, Date.from(Instant.now()), period, action)
}

fun newSingleThreadContext(): CoroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()