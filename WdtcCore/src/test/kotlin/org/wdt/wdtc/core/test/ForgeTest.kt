package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.download.forge.ForgeVersionList
import org.wdt.wdtc.core.game.Version
import kotlin.test.Test

class ForgeTest {
	private val version = Version("1.17.1")
	
	@Test
	fun printVersions() {
		ForgeVersionList(version).versionList.forEach {
			println(it.versionNumber)
		}
	}
}