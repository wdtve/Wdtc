package org.wdt

import org.wdt.wdtc.core.openapi.plugins.interfaces.StartupAction
import kotlin.test.Test

class Test {
	@Test
	fun test() {
	}
}

class Tests : StartupAction {
	override fun invoke() {
		println("hello")
	}
	
}