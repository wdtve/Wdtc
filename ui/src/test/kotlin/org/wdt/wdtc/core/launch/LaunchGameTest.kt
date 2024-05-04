package org.wdt.wdtc.core.launch

import org.junit.jupiter.api.Test
import org.wdt.utils.io.toFile
import java.net.URLClassLoader

class LaunchGameTest {
	
	@Test
	fun getTaskList() {
		val loader = URLClassLoader(arrayOf("E:\\Wdtc\\.wdtc\\dependencies\\org\\openjfx\\javafx-graphics\\17.0.6\\javafx-graphics-17.0.6-win.jar".toFile().toURI().toURL()))
		loader.loadClass("javafx.application.Application")
	}
}