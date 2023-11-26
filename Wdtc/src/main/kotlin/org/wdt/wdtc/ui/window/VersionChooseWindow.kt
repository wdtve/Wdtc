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
import org.wdt.wdtc.core.utils.ModUtils.getModTask
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.Consoler.setTopLowerLeft
import org.wdt.wdtc.ui.window.Consoler.setTopLowerRight

class VersionChooseWindow(private val path: GameDirectoryManger?) {
  fun setWindow(MainStage: Stage) {
    val size = WindwosSizeManger(MainStage)
    val ParentPane = AnchorPane()
    val SonScrollPane = ScrollPane()
    AnchorPane.setLeftAnchor(SonScrollPane, 100.0)
    SonScrollPane.setTopLowerRight()
    AnchorPane.setTopAnchor(SonScrollPane, 70.0)
    val VersionList = VBox()
    val back = JFXButton("返回")
    back.onAction = EventHandler { event: ActionEvent? ->
      val window: HomeWindow
      window = (path as? Launcher)?.let { HomeWindow(it) } ?: HomeWindow()
      window.setHome(MainStage)
    }
    if (isDownloadedGame(path!!)) {
      val GameVersionList: List<Launcher>? = getGameVersionList(
        path
      )
      for (GameVersion in GameVersionList!!) {
        val pane = Pane()
        pane.setPrefSize(514.0, 40.0)
        val VersionId = RadioButton(GameVersion.versionNumber)
        if (preferredLauncher != null && preferredLauncher!!.equals(GameVersion)) {
          VersionId.isSelected = true
        }
        VersionId.layoutX = 14.0
        VersionId.layoutY = 12.0
        getModTask(GameVersion)
        val ModKind = Label()
        if (GameVersion.kind != KindOfMod.Original) {
          ModKind.text = "Mod : " + GameVersion.kind.toString()
        } else {
          ModKind.text = "Mod : 无"
        }
        ModKind.layoutX = 86.0
        ModKind.layoutY = 13.0
        size.modifyWindwosSize(pane, VersionId, ModKind)
        size.modifyWindwosSize(VersionList, pane)
        VersionId.onAction = EventHandler { event: ActionEvent? ->
          val setting = setting
          setting.preferredVersion = VersionId.text
          putSettingToFile(setting)
          val win = HomeWindow(GameVersion)
          win.setHome(MainStage)
        }
      }
    }
    VersionList.setPrefSize(517.0, 416.0)
    SonScrollPane.content = VersionList
    val NewGame = JFXButton("安装新游戏")
    NewGame.setPrefSize(100.0, 30.0)
    NewGame.styleClass.add("BackGroundWriteButton")
    NewGame.setTopLowerLeft()
    val tips = Label("\t当前目录")
    tips.prefWidth = 96.0
    tips.layoutY = 70.0
    ParentPane.children.addAll(NewGame, back, SonScrollPane, tips)
    ParentPane.background = Consoler.background
    ParentPane.setStylesheets()
    Consoler.setCss("BlackBorder", back)
    MainStage.setScene(Scene(ParentPane))
    NewGame.onAction = EventHandler { event: ActionEvent? -> GameVersionListWindow.setWindowScene(MainStage) }
  }
}
