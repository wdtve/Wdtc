package org.wdt.wdtc.core.launch

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.wdt.wdtc.ui.loader.setJavaFXListJson

class LaunchGameTest {
	
	@Test
	fun getTaskList() {
		runBlocking {
			setJavaFXListJson()
		}
	}
}