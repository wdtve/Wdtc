package org.wdt.wdtc.core.openapi.manager

import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.warning
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MultithreadingManager(
	spend: Int, var context: CoroutineContext = EmptyCoroutineContext
) : TaskExecutor {
	
	private val count: CountDownLatch = CountDownLatch(spend)
	
	@Synchronized
	fun countDown() {
		count.countDown()
	}
	
	fun await() {
		count.await()
	}
	
	override fun before() {
	}
	
	override suspend fun run(task: AbstractTask) {
		context.launch {
			task.start()
		}
	}
	
	override fun after() {
		countDown()
	}
	
	suspend inline fun doExecuters(block: () -> Iterable<RunTaskExecutor>) {
		block().forEach {
			doExecuter(it)
		}
		await()
	}
	
	suspend fun doExecuter(task: RunTaskExecutor) {
		before()
		run(object : AbstractTask() {
			override suspend fun start() {
				try {
					task.doExecutor()
					after()
				} catch (e: Throwable) {
					logmaker.warning(e.message.let { it ?: "Exception:" }, e)
					after()
				}
			}
		})
	}
}