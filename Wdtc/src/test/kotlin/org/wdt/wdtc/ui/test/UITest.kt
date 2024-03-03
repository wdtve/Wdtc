package org.wdt.wdtc.ui.test

import org.wdt.wdtc.core.manger.ckeckVMConfig
import org.wdt.wdtc.core.manger.runStartUpTask
import org.wdt.wdtc.ui.setJavaFXListJson
import kotlin.test.Test

class UITest {
  @Test
  fun setListJson() {
    ckeckVMConfig()
    runStartUpTask()
    setJavaFXListJson()
  }
}