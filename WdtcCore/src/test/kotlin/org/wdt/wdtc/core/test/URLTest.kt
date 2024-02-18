package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.utils.toURL
import java.net.URL
import kotlin.test.Test

class URLTest {
  @Test
  fun testUrlbuilder() {
    println(URL("https://www.jetbrains.com.cn/".toURL(), "idea/download"))
  }
}
