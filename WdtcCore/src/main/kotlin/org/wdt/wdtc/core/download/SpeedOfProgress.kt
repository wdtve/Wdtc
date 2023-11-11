package org.wdt.wdtc.core.download

import java.util.concurrent.CountDownLatch
import kotlin.concurrent.Volatile

class SpeedOfProgress(@field:Volatile private var spend: Int) {
    private val countDown: CountDownLatch = CountDownLatch(spend)

    @Synchronized
    fun countDown() {
        spend -= 1
        countDown.countDown()
    }

    fun await() {
        countDown.await()
    }

    val isSpendZero: Boolean
        get() = spend == 0
}
