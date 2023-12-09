package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.downloadSourceKind
import org.wdt.wdtc.core.utils.openSomething
import kotlin.concurrent.thread

class GameDownloadingWindow(private val launcher: Launcher) {
  fun setDownGameWin(mainStage: Stage) {
    val size = WindwosSizeManger(mainStage)
    mainStage.title = Consoler.getWindowsTitle("下载游戏")
    val pane = AnchorPane()
    val back = JFXButton("返回")
    back.onAction = EventHandler {
      val win = HomeWindow()
      win.setHome(mainStage)
    }
    back.style = "-fx-border-color: #000000"
    val textField = TextField()
    textField.promptText = "三个阶段"
    AnchorPane.setTopAnchor(textField, 350.0)
    AnchorPane.setLeftAnchor(textField, 150.0)
    AnchorPane.setRightAnchor(textField, 150.0)
    val time = Label("下载时间不会太长")
    val statusBar = Label("下面是状态栏")
    time.layoutX = 240.0
    time.layoutY = 160.0
    statusBar.layoutX = 253.0
    statusBar.layoutY = 305.0
    val bmclHome = Button("BMCLAPI")
    bmclHome.onAction = EventHandler { openSomething("https://bmclapidoc.bangbang93.com/") }
    AnchorPane.setRightAnchor(bmclHome, 0.0)
    AnchorPane.setTopAnchor(bmclHome, 0.0)
    val openBmcl = Label("国内快速下载源→")
    AnchorPane.setRightAnchor(openBmcl, 70.0)
    AnchorPane.setTopAnchor(openBmcl, 4.0)
    textField.text = launcher.versionNumber + "开始下载,下载源: " + downloadSourceKind
      thread(name = "Download ${launcher.versionNumber} task") {
        val installGameVersion = InstallGameVersion(launcher, true)
        installGameVersion.setTextFieldText = TextInterface { value: String? -> textField.text = value }
        installGameVersion.startInstallGame()
      }
    pane.background = Consoler.background
    size.modifyWindwosSize(pane, back, time, statusBar, bmclHome, openBmcl, textField)
    val downScene = Scene(pane)
    mainStage.setScene(downScene)
  }
}
