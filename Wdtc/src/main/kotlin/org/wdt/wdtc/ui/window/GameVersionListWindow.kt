package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile.Companion.startDownloadVersionManifestJsonFile
import org.wdt.wdtc.core.download.game.GameVersionList
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.GameFileManger.Companion.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.Consoler.setTopGrid

object GameVersionListWindow {
  fun setWindowScene(MainStage: Stage) {
    downloadVersionManifestJsonFileTask()
    val size = WindwosSizeManger(MainStage)
    val pane = AnchorPane()
    val list = VBox()
    list.setTopGrid()
    val sp = ScrollPane()
    val refreshButton = JFXButton("刷新")
    refreshButton.setPrefSize(155.0, 30.0)
    refreshButton.styleClass.add("BackGroundWriteButton")
    AnchorPane.setBottomAnchor(refreshButton, 0.0)
    AnchorPane.setLeftAnchor(refreshButton, 0.0)
    AnchorPane.setLeftAnchor(sp, 155.0)
    AnchorPane.setTopAnchor(sp, 0.0)
    AnchorPane.setBottomAnchor(sp, 0.0)
    AnchorPane.setRightAnchor(sp, 0.0)
    val back = JFXButton("返回")
    back.styleClass.add("BlackBorder")
    val tips = Label("选择右侧的一个版本")
    tips.layoutX = 27.0
    tips.layoutY = 71.0
    back.onAction = EventHandler {
      val win = HomeWindow()
      win.setHome(MainStage)
    }
    refreshButton.onAction = EventHandler {
      startDownloadVersionManifestJsonFile()
      setWindowScene(MainStage)
    }
    Platform.runLater {
      val versionlist = GameVersionList().versionList
      for (s in versionlist) {
        val button = JFXButton(s.versionNumber)
        button.prefWidth = 458.0
        size.modifyWindwosSize(list, button)
        button.styleClass.add("BlackBorder")
        button.onAction = EventHandler {
          val launcher = Launcher(button.text)
          val choose = ModChooseWindow(launcher, MainStage)
          choose.setChooseWin()
        }
      }
    }
    sp.content = list
    sp.layoutX = 155.0
    sp.prefHeight = WindwosSizeManger.Companion.windowsHeight
    sp.prefWidth = 461.0
    pane.children.addAll(sp, back, tips, refreshButton)
    pane.setStylesheets()
    pane.background = Consoler.background
    MainStage.title = Consoler.getWindowsTitle("下载游戏")
    MainStage.setScene(Scene(pane, 600.0, 450.0))
  }
}
