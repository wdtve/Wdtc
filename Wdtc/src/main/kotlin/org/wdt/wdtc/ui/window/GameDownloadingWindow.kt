package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
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
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSourceKind
import org.wdt.wdtc.core.utils.URLUtils.openSomething
import kotlin.concurrent.thread

class GameDownloadingWindow(private val launcher: Launcher) {
  fun setDownGameWin(MainStage: Stage) {
    val size = WindwosSizeManger(MainStage)
    MainStage.title = Consoler.getWindowsTitle("下载游戏")
    val pane = AnchorPane()
    val back = JFXButton("返回")
    back.onAction = EventHandler {
      val win = HomeWindow()
      win.setHome(MainStage)
    }
    back.style = "-fx-border-color: #000000"
    val textField = TextField()
    textField.promptText = "三个阶段"
    AnchorPane.setTopAnchor(textField, 350.0)
    AnchorPane.setLeftAnchor(textField, 150.0)
    AnchorPane.setRightAnchor(textField, 150.0)
    val time = Label("下载时间不会太长")
    val status_bar = Label("下面是状态栏")
    time.layoutX = 240.0
    time.layoutY = 160.0
    status_bar.layoutX = 253.0
    status_bar.layoutY = 305.0
    val bmclHome = Button("BMCLAPI")
    bmclHome.onAction = EventHandler { event: ActionEvent? -> openSomething("https://bmclapidoc.bangbang93.com/") }
    AnchorPane.setRightAnchor(bmclHome, 0.0)
    AnchorPane.setTopAnchor(bmclHome, 0.0)
    val read_bmcl = Label("国内快速下载源→")
    AnchorPane.setRightAnchor(read_bmcl, 70.0)
    AnchorPane.setTopAnchor(read_bmcl, 4.0)
    textField.text = launcher.versionNumber + "开始下载,下载源: " + downloadSourceKind
    thread(name = "Download ${launcher.versionNumber} task") {
      val installGameVersion = InstallGameVersion(launcher, true)
      installGameVersion.setTextFieldText = TextInterface { value: String? -> textField.text = value }
      installGameVersion.startInstallGame()
    }
    pane.background = Consoler.background
    size.modifyWindwosSize(pane, back, time, status_bar, bmclHome, read_bmcl, textField)
    val downScene = Scene(pane)
    MainStage.setScene(downScene)
  }
}
