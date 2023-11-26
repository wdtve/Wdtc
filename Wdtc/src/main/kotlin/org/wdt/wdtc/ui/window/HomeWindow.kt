package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.auth.User.Companion.isExistUserJsonFile
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.download.infterface.TextInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.Launcher.Companion.preferredLauncher
import org.wdt.wdtc.core.launch.LaunchGame.Companion.create
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.VMManger.launcherVersion
import org.wdt.wdtc.core.utils.ThreadUtils.startThread
import org.wdt.wdtc.core.utils.URLUtils.isOnline
import org.wdt.wdtc.core.utils.URLUtils.openSomething
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.user.NewUserWindows
import java.io.IOException
import java.util.*

class HomeWindow {
  private var launcher = preferredLauncher

  constructor(launcher: Launcher?) {
    this.launcher = launcher
  }

  constructor()

  fun setHome(MainStage: Stage) {
    val pane = AnchorPane()
    val windwosSizeManger = WindwosSizeManger(MainStage)
    MainStage.title = Consoler.windowsTitle
    val Menu = VBox()
    Menu.setPrefSize(128.0, 450.0)
    val home = JFXButton("首页")
    home.setPrefSize(128.0, 46.0)
    val UserWindow = JFXButton("修改账户")
    UserWindow.setPrefSize(128.0, 46.0)
    UserWindow.onAction = EventHandler {
      val windows = NewUserWindows(MainStage)
      windows.title = "注册账户"
      windows.show()
    }
    val downgame = JFXButton("下载游戏")
    downgame.isDisable = !isOnline
    downgame.setPrefSize(128.0, 46.0)
    downgame.onAction = EventHandler { GameVersionListWindow.setWindowScene(MainStage) }
    val startgame = JFXButton("选择版本")
    startgame.layoutY = 69.0
    startgame.setPrefSize(128.0, 46.0)
    startgame.onAction = EventHandler {
      val choose = VersionChooseWindow(Objects.requireNonNullElseGet(launcher) { GameDirectoryManger() })
      choose.setWindow(MainStage)
    }
    val VersionSetting = JFXButton("版本设置")
    VersionSetting.setPrefSize(128.0, 46.0)
    if (launcher != null) {
      VersionSetting.isDisable = false
      val windows = VersionSettingWindow(launcher!!, MainStage)
      VersionSetting.onAction = EventHandler { windows.setWindow() }
    } else {
      VersionSetting.isDisable = true
    }
    val setting = getSettingButton(MainStage)
    val github = JFXButton("GitHub")
    github.setPrefSize(128.0, 46.0)
    github.onAction = EventHandler { openSomething("https://github.com/Wd-t/Wdtc") }
    val name = Label(
      """
      Wdtc
      $launcherVersion
      """.trimIndent()
    )
    name.layoutX = 17.0
    name.layoutY = 161.0
    val readme = Label("一个简单到不能再简单的我的世界Java版启动器")
    readme.layoutX = 180.0
    readme.layoutY = 180.0
    readme.styleClass.add("readme")
    val LaunchGameButton = JFXButton()
    if (launcher != null) {
      LaunchGameButton.text = """
            启动游戏
            ${launcher!!.versionNumber}
            """.trimIndent()
    } else {
      LaunchGameButton.text = "当前无游戏版本"
    }
    LaunchGameButton.setPrefSize(227.0, 89.0)
    LaunchGameButton.layoutX = 335.0
    LaunchGameButton.layoutY = 316.0
    LaunchGameButton.styleClass.add("BackGroundWriteButton")
    AnchorPane.setBottomAnchor(LaunchGameButton, 30.0)
    AnchorPane.setRightAnchor(LaunchGameButton, 30.0)
    LaunchGameButton.onAction = EventHandler { event: ActionEvent? ->
      if (launcher != null) {
        if (isExistUserJsonFile) {
          Runnable {
            try {
              val launch = create(launcher!!)
              val launchProcess = launch.launchProcess
              launchProcess.setUIText =
                TextInterface { string: String? -> ExceptionWindow.setWin(string, "Launch Error") }
              launchProcess.startLaunchGame()
            } catch (e: IOException) {
              ExceptionWindow.setErrorWin(e)
            }
          }.startThread().setName("Launch Game")
        } else {
          noUser(MainStage)
        }
      } else {
        GameVersionListWindow.setWindowScene(MainStage)
      }
    }
    Menu.children.addAll(home, UserWindow, downgame, startgame, VersionSetting, setting, github)
    Menu.styleClass.add("BlackBorder")
    AnchorPane.setTopAnchor(Menu, 0.0)
    AnchorPane.setBottomAnchor(Menu, 0.0)
    AnchorPane.setLeftAnchor(Menu, 0.0)
    pane.children.addAll(Menu, LaunchGameButton)
    windwosSizeManger.modifyWindwosSize(pane, readme)
    pane.setStylesheets()
    pane.background = Consoler.background
    val scene = Scene(pane, 600.0, 450.0)
    MainStage.setScene(scene)
    if (!isExistUserJsonFile) {
      noUser(MainStage)
    }
  }

  companion object {
    private val logmaker = getLogger(HomeWindow::class.java)
    private fun getSettingButton(MainStage: Stage): JFXButton {
      val setting = JFXButton("设置")
      setting.setPrefSize(128.0, 46.0)
      setting.onMousePressed = EventHandler { event: MouseEvent ->
        if (event.isControlDown) {
          openSomething(wdtcCache)
        } else {
          try {
            SettingWindow.setSettingWin(MainStage)
          } catch (e: IOException) {
            ExceptionWindow.setErrorWin(e)
          }
        }
      }
      return setting
    }

    private fun noUser(MainStage: Stage) {
      val windows = NewUserWindows(MainStage)
      windows.title = "您还没有账户呢!"
      windows.type = AccountsType.Offline
      windows.show()
    }
  }
}
