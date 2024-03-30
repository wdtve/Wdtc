package org.wdt.wdtc.core.test

import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import kotlin.test.Test

class IOTest {
	@Test
	fun test() {
		runBlocking {
			startDownloadTask(
				"https://piston-data.mojang.com/v1/objects/be76ecc174ea25580bdc9bf335481a5192d9f3b7/client.txt".toURL() to wdtcCache.resolve(
					"test.txt"
				)
			)
		}
	}
}
