package org.wdt.wdtc.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.stage.Stage
import org.wdt.utils.io.touch
import org.wdt.wdtc.core.manger.SettingManger.Companion.putSettingToFile
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.manger.VMManger.isDebug
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.StopProcess
import org.wdt.wdtc.core.utils.URLUtils.isOnline
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import org.wdt.wdtc.ui.window.Consoler
import org.wdt.wdtc.ui.window.ExceptionWindow
import org.wdt.wdtc.ui.window.HomeWindow
import org.wdt.wdtc.ui.window.WindwosSizeManger
import org.wdt.wdtc.ui.window.WindwosSizeManger.Companion.getSizeManger
import java.io.IOException

class AppMain : Application() {
  private val logmaker = getLogger(AppMain::class.java)
  override fun start(mainStage: Stage) {
    try {
      val size = mainStage.getSizeManger()
      if (isOnline) {
        mainStage.title = Consoler.windowsTitle
      } else {
        mainStage.title = Consoler.getWindowsTitle("无网络")
        logmaker.warn("当前无网络连接,下载功能无法正常使用!")
      }
      mainStage.minWidth = WindwosSizeManger.windowsWidht
      mainStage.minHeight = WindwosSizeManger.windowsHeight
      size.setWindwosSize()
      mainStage.icons.add(Image("assets/icon/ico.jpg"))
      mainStage.isResizable = isDebug
      val win = HomeWindow()
      win.setHome(mainStage)
      mainStage.show()
      logmaker.info("Window Show")
      mainStage.onCloseRequest = EventHandler {
        logmaker.info(size)
        try {
          val setting = setting
          setting.windowsWidth = mainStage.width
          setting.windowsHeight = mainStage.height
          StopProcess.touch()
          putSettingToFile(setting)
        } catch (e: IOException) {
          logmaker.error(e.getExceptionMessage())
        }
        Platform.exit()
        logmaker.info("======= Wdtc Stop ========")
      }
    } catch (e: Exception) {
      ExceptionWindow.setErrorWin(e)
    }
  }
//  @JvmStatic
//  fun main(args: Array<String>) {
//    WdtcMain.main(args)
//    launch(AppMain::class.java, *args)
//  }
}
