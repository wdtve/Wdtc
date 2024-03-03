package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.auth.isExistUserJsonFile
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.preferredVersion
import org.wdt.wdtc.core.launch.LaunchGame
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.ui.window.user.NewUserWindows
import java.io.IOException

class HomeWindow {
	private var version = preferredVersion
	
	constructor(version: Version?) {
		this.version = version
	}
	
	constructor()
	
	fun setHome(mainStage: Stage) {
		val windwosSizeManger = mainStage.run {
			title = windowsTitle
			WindwosSizeManger(mainStage)
		}
		val home = JFXButton("首页").apply {
			setPrefSize(128.0, 46.0)
		}
		val userWindow = JFXButton("修改账户").apply {
			setPrefSize(128.0, 46.0)
			onAction = EventHandler {
				NewUserWindows(mainStage).apply {
					title = "注册账户"
				}.run { show() }
			}
		}
		val downgame = JFXButton("下载游戏").apply {
			isDisable = !isOnline
			setPrefSize(128.0, 46.0)
			onAction = EventHandler { GameVersionListWindow.setWindowScene(mainStage) }
		}
		val startgame = JFXButton("选择版本").apply {
			layoutY = 69.0
			setPrefSize(128.0, 46.0)
			onAction = EventHandler {
				VersionChooseWindow(version ?: GameDirectoryManger()).run {
					setWindow(mainStage)
				}
			}
		}
		val versionSetting = JFXButton("版本设置").apply {
			setPrefSize(128.0, 46.0)
			isDisable = version.let {
				if (it != null) {
					onAction = EventHandler { _ -> VersionSettingWindow(it, mainStage).setWindow() }
					false
				} else {
					true
				}
			}
		}
		val setting = getSettingButton(mainStage)
		val github = JFXButton("GitHub").apply {
			setPrefSize(128.0, 46.0)
			onAction = EventHandler { openSomething("https://github.com/wd-t/Wdtc") }
		}
		val launchGameButton = JFXButton().apply {
			text = version.let { if (it != null) "启动游戏${it.versionNumber}" else "当前无游戏版本" }
			setPrefSize(227.0, 89.0)
			layoutX = 335.0
			layoutY = 316.0
			styleClass.add("BackGroundWriteButton")
			onAction = EventHandler {
				version.let {
					if (it != null) {
						if (isExistUserJsonFile) {
							launchGame(mainStage, it)
						} else {
							noUser(mainStage)
						}
					} else {
						GameVersionListWindow.setWindowScene(mainStage)
					}
				}
			}
			setBottomAnchor(30.0)
			setRightAnchor(30.0)
		}
		val menu = VBox().apply {
			setPrefSize(128.0, 450.0)
			children.addAll(home, userWindow, downgame, startgame, versionSetting, setting, github)
			styleClass.add("BlackBorder")
			setTopAnchor(0.0)
			setBottomAnchor(0.0)
			setLeftAnchor(0.0)
		}
		val pane = AnchorPane().apply {
			children.addAll(menu, launchGameButton)
			setStylesheets()
			this.background = wdtcBackground
		}
		Scene(pane, 600.0, 450.0).let {
			mainStage.scene = it
		}
		if (!isExistUserJsonFile) {
			javafxCoroutineScope.launch("Show user window") {
				noUser(mainStage)
			}
		}
	}
	
	companion object {
		private fun getSettingButton(mainStage: Stage): JFXButton {
			return JFXButton("设置").apply {
				setPrefSize(128.0, 46.0)
				onMousePressed = EventHandler { event: MouseEvent ->
					if (event.isControlDown) {
						scwn("Open cache directory") {
							openSomething(wdtcCache)
						}
					} else {
						try {
							SettingWindow.setSettingWin(mainStage)
						} catch (e: IOException) {
							setErrorWin(e)
						}
					}
				}
			}
		}
		
		fun launchGame(mainStage: Stage, version: Version) {
			LaunchGame(version.noNull(), TaskWindow.taskPool).run {
				getTaskList { setWin(it, "启动失败") }.let {
					TaskWindow(mainStage, it).run {
						showTaskStage()
					}
				}
			}
			
		}
		
		private fun noUser(mainStage: Stage) {
			NewUserWindows(mainStage).apply {
				title = "您还没有账户呢!"
				type = AccountsType.OFFLINE
			}.run { show() }
		}
	}
}
