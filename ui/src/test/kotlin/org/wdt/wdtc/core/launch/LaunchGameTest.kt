package org.wdt.wdtc.core.launch

import org.junit.jupiter.api.Test
import org.wdt.wdtc.core.impl.launch.LaunchGame.Companion.launchGameTask
import org.wdt.wdtc.core.openapi.game.Version

class LaunchGameTest {
	
	@Test
	fun getTaskList() {
		println(Version("1.20.4").launchGameTask.getTaskList { println(it) })
	}
}