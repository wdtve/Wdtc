package org.wdt.wdtc.core.launch

import org.junit.jupiter.api.Test
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.launch.LaunchGame.Companion.launchGameTask

class LaunchGameTest {
	
	@Test
	fun getTaskList() {
		println(Version("1.20.4").launchGameTask.getTaskList { println(it) })
	}
}