package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.ckeckVMConfig
import org.wdt.wdtc.core.utils.gson.serializeLauncherGson
import kotlin.test.Test

class JsonTest {
  @Test
  fun testLauncher() {
    System.setProperty("wdtc.debug.switch", true.toString())
    ckeckVMConfig()
    val launcher = Launcher("1.20.1")
    launcher.quiltModDownloadInfo = QuiltDownloadInfo(launcher, "114514")
    println(serializeLauncherGson.create().toJson(launcher))
  }
}
