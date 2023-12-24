package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.auth.isExistUserJsonFile
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.preferredLauncher
import org.wdt.wdtc.core.launch.LaunchGame
import org.wdt.wdtc.core.launch.LaunchProcess
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.launcherVersion
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.openSomething
import org.wdt.wdtc.ui.window.user.NewUserWindows
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

class HomeWindow {
  private var launcher = preferredLauncher

  constructor(launcher: Launcher?) {
    this.launcher = launcher
  }

  constructor()

  fun setHome(mainStage: Stage) {
    val pane = AnchorPane()
    val windwosSizeManger = WindwosSizeManger(mainStage)
    mainStage.title = windowsTitle
    val menu = VBox()
    menu.setPrefSize(128.0, 450.0)
    val home = JFXButton("首页")
    home.setPrefSize(128.0, 46.0)
    val userWindow = JFXButton("修改账户")
    userWindow.setPrefSize(128.0, 46.0)
    userWindow.onAction = EventHandler {
      val windows = NewUserWindows(mainStage)
      windows.title = "注册账户"
      windows.show()
    }
    val downgame = JFXButton("下载游戏")
    downgame.isDisable = !isOnline
    downgame.setPrefSize(128.0, 46.0)
    downgame.onAction = EventHandler { GameVersionListWindow.setWindowScene(mainStage) }
    val startgame = JFXButton("选择版本")
    startgame.layoutY = 69.0
    startgame.setPrefSize(128.0, 46.0)
    startgame.onAction = EventHandler {
      val choose = VersionChooseWindow(Objects.requireNonNullElseGet(launcher) { GameDirectoryManger() }!!)
      choose.setWindow(mainStage)
    }
    val versionSetting = JFXButton("版本设置")
    versionSetting.setPrefSize(128.0, 46.0)
    if (launcher != null) {
      versionSetting.isDisable = false
      val windows = VersionSettingWindow(launcher!!, mainStage)
      versionSetting.onAction = EventHandler { windows.setWindow() }
    } else {
      versionSetting.isDisable = true
    }
    val setting = getSettingButton(mainStage)
    val github = JFXButton("GitHub")
    github.setPrefSize(128.0, 46.0)
    github.onAction = EventHandler { openSomething("https://github.com/wd-t/Wdtc") }
    val name = Label("Wdtc\n$launcherVersion")
    name.layoutX = 17.0
    name.layoutY = 161.0
    val readme = Label("一个简单到不能再简单的我的世界Java版启动器")
    readme.layoutX = 180.0
    readme.layoutY = 180.0
    readme.styleClass.add("readme")
    val launchGameButton = JFXButton()
    launchGameButton.text = if (launcher != null) "启动游戏${launcher!!.versionNumber}" else "当前无游戏版本"
    launchGameButton.setPrefSize(227.0, 89.0)
    launchGameButton.layoutX = 335.0
    launchGameButton.layoutY = 316.0
    launchGameButton.styleClass.add("BackGroundWriteButton")
    AnchorPane.setBottomAnchor(launchGameButton, 30.0)
    AnchorPane.setRightAnchor(launchGameButton, 30.0)
    launchGameButton.onAction = EventHandler {
      if (launcher != null) {
        if (isExistUserJsonFile) {
          launchGame(launcher!!)
        } else {
          noUser(mainStage)
        }
      } else {
        GameVersionListWindow.setWindowScene(mainStage)
      }
    }
    menu.children.addAll(home, userWindow, downgame, startgame, versionSetting, setting, github)
    menu.styleClass.add("BlackBorder")
    AnchorPane.setTopAnchor(menu, 0.0)
    AnchorPane.setBottomAnchor(menu, 0.0)
    AnchorPane.setLeftAnchor(menu, 0.0)
    pane.children.addAll(menu, launchGameButton)
    windwosSizeManger.modifyWindwosSize(pane, readme)
    pane.setStylesheets()
    pane.background = background
    val scene = Scene(pane, 600.0, 450.0)
    mainStage.scene = scene
    if (!isExistUserJsonFile) {
      noUser(mainStage)
    }
  }

  companion object {
    private fun getSettingButton(mainStage: Stage): JFXButton {
      val setting = JFXButton("设置")
      setting.setPrefSize(128.0, 46.0)
      setting.onMousePressed = EventHandler { event: MouseEvent ->
        if (event.isControlDown) {
          openSomething(wdtcCache)
        } else {
          try {
            SettingWindow.setSettingWin(mainStage)
          } catch (e: IOException) {
            setErrorWin(e)
          }
        }
      }
      return setting
    }

    private fun noUser(mainStage: Stage) {
      val windows = NewUserWindows(mainStage)
      windows.title = "您还没有账户呢!"
      windows.type = AccountsType.Offline
      windows.show()
    }

    private fun launchGame(launcher: Launcher) {
      try {
        thread(name = "${launcher.versionNumber} launch task") {
          val launchProcess = LaunchProcess(LaunchGame.create(launcher).launchTaskProcess) {
            setWin(it, "Launch error")
          }
          launchProcess.startLaunchGame()
        }
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
  }
}
