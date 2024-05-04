package org.wdt.wdtc.core.openapi.manger

import kotlinx.coroutines.CoroutineScope
import org.wdt.wdtc.core.openapi.utils.ioCoroutineScope
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.warning
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.Volatile

class ProgressManger(
	@field:Volatile private var spend: Int = -1,
	var coroutineScope: CoroutineScope = ioCoroutineScope
) {
	
	private val countDown: CountDownLatch = CountDownLatch(spend)
	
	@Synchronized
	fun countDown() {
		require(spend >= 0) { "The number of task is negative, invalid" }
		spend -= 1
		countDown.countDown()
	}
	
	fun await() {
		require(spend >= 0) { "The number of task is negative, invalid" }
		countDown.await()
	}
	
	
	val isSpendZero: Boolean
		get() = spend == 0
}

inline fun ProgressManger.finishCountDown(run: Boolean, block: () -> Unit) {
	if (run) {
		try {
			block()
			countDown()
		} catch (e: Throwable) {
			logmaker.warning(e.message.noNull(), e)
			countDown()
		}
	} else {
		countDown()
	}
}


