package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.game.determineSize
import kotlin.test.Test

class StringTest {
  @Test
  fun testReagx() {
    val regex = ".*\\$\\{(.*)}".toRegex()
    println("\${auth_player_name}".matches(regex))
    println(regex.find("\${auth_player_name}")?.groups?.get(1)?.value)
    println("-Dminecraft.launcher.version=\${launcher_version}".matches(regex))
  }

  @Test
  fun testName() {
    println(determineSize("0.1.2", "0.1.1"))
  }

}