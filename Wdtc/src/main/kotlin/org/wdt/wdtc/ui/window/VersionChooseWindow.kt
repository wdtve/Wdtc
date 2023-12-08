package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.game.DownloadedGameVersion.getGameVersionList
import org.wdt.wdtc.core.game.DownloadedGameVersion.isDownloadedGame
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.Launcher.Companion.preferredLauncher
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.SettingManger.Companion.putSettingToFile
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.Consoler.setTopLowerLeft
import org.wdt.wdtc.ui.window.Consoler.setTopLowerRight

class VersionChooseWindow(private val path: GameDirectoryManger) {
  fun setWindow(mainStage: Stage) {
    val size = WindwosSizeManger(mainStage)
    val parentPane = AnchorPane()
    val sonScrollPane = ScrollPane()
    AnchorPane.setLeftAnchor(sonScrollPane, 100.0)
    sonScrollPane.setTopLowerRight()
    AnchorPane.setTopAnchor(sonScrollPane, 70.0)
    val versionList = VBox()
    val back = JFXButton("返回")
    back.onAction = EventHandler {
      val window = (path as? Launcher)?.let { HomeWindow(it) } ?: HomeWindow()
      window.setHome(mainStage)
    }
    if (path.isDownloadedGame) {
      val gameVersionList: List<Launcher> = path.getGameVersionList()!!
      for (gameVersion in gameVersionList) {
        val pane = Pane()
        pane.setPrefSize(514.0, 40.0)
        val versionId = RadioButton(gameVersion.versionNumber)
        if (preferredLauncher != null && preferredLauncher == gameVersion) {
          versionId.isSelected = true
        }
        versionId.layoutX = 14.0
        versionId.layoutY = 12.0
        val modKind = Label()
        if (gameVersion.kind != KindOfMod.ORIGINAL) {
          modKind.text = "Mod : " + gameVersion.kind.toString()
        } else {
          modKind.text = "Mod : 无"
        }
        modKind.layoutX = 86.0
        modKind.layoutY = 13.0
        size.modifyWindwosSize(pane, versionId, modKind)
        size.modifyWindwosSize(versionList, pane)
        versionId.onAction = EventHandler { event: ActionEvent? ->
          val setting = setting
          setting.preferredVersion = versionId.text
          putSettingToFile(setting)
          val win = HomeWindow(gameVersion)
          win.setHome(mainStage)
        }
      }
    }
    versionList.setPrefSize(517.0, 416.0)
    sonScrollPane.content = versionList
    val newGame = JFXButton("安装新游戏")
    newGame.setPrefSize(100.0, 30.0)
    newGame.styleClass.add("BackGroundWriteButton")
    newGame.setTopLowerLeft()
    val tips = Label("\t当前目录")
    tips.prefWidth = 96.0
    tips.layoutY = 70.0
    parentPane.children.addAll(newGame, back, sonScrollPane, tips)
    parentPane.background = Consoler.background
    parentPane.setStylesheets()
    Consoler.setCss("BlackBorder", back)
    mainStage.setScene(Scene(parentPane))
    newGame.onAction = EventHandler { event: ActionEvent? -> GameVersionListWindow.setWindowScene(mainStage) }
  }
}
