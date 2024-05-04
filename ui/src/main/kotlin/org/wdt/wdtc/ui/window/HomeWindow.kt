package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.*
import org.wdt.wdtc.core.impl.launch.LaunchGame
import org.wdt.wdtc.core.openapi.auth.Accounts.AccountsType
import org.wdt.wdtc.core.openapi.auth.isExistUserJsonFile
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.preferredVersion
import org.wdt.wdtc.core.openapi.manger.GameDirectoryManger
import org.wdt.wdtc.core.openapi.manger.wdtcCache
import org.wdt.wdtc.core.openapi.utils.isOnline
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.openSomething
import org.wdt.wdtc.core.openapi.utils.runOnIO
import org.wdt.wdtc.ui.window.user.NewUserWindows

class HomeWindow {
	private var version = preferredVersion
	
	constructor(version: Version?) {
		this.version = version
	}
	
	constructor()
	
	suspend fun setHome(mainStage: Stage) {
		runOnJavaFx {
			mainStage.run {
				title = windowsTitle
				getSizeManger()
			}
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
			onAction = EventHandler { GameVersionListWindow().setWindowScene(mainStage) }
		}
		val startgame = JFXButton("选择版本").apply {
			layoutY = 69.0
			setPrefSize(128.0, 46.0)
			onAction = EventHandler {
				VersionChooseWindow(version ?: GameDirectoryManger.DEFAULT_GAME_DIRECTORY).run {
					setWindow(mainStage)
				}
			}
		}
		val versionSetting = JFXButton("版本设置").apply {
			setPrefSize(128.0, 46.0)
			isDisable = version.let {
				if (it != null) {
					onAction = eventHandler {
						VersionSettingWindow(it, mainStage).setWindow()
					}
					false
				} else {
					true
				}
			}
		}
		val setting = getSettingButton(mainStage)
		val github = JFXButton("GitHub").apply {
			setPrefSize(128.0, 46.0)
			onAction = EventHandler { openSomething("https://github.com/wdtve/Wdtc") }
		}
		val launchGameButton = JFXButton().apply {
			text = version.let { if (it != null) "启动游戏${it.versionNumber}" else "当前无游戏版本" }
			setPrefSize(227.0, 89.0)
			layoutX = 335.0
			layoutY = 316.0
			styleClass.add("BackGroundWriteButton")
			onAction = eventHandler {
				withContext(Dispatchers.Default) {
					launchGameButtonTask(mainStage, version)
				}
			}
			setBottomAnchor(30.0)
			setRightAnchor(30.0)
		}
		val menu = VBox().apply {
			setPrefSize(128.0, 450.0)
			styleClass.add("BlackBorder")
			setTopAnchor(0.0)
			setBottomAnchor(0.0)
			setLeftAnchor(0.0)
			children.addAll(
				home, userWindow, downgame, startgame, versionSetting, setting.await(), github
			)
		}
		val pane = AnchorPane().apply {
			background = wdtcBackground
			children.addAll(menu, launchGameButton)
			setStylesheets()
		}
		runOnJavaFx {
			Scene(pane, 600.0, 450.0).let {
				mainStage.scene = it
			}
		}
		if (!isExistUserJsonFile) {
			runOnJavaFx {
				noUser(mainStage)
			}
		}
	}
	
	companion object {
		private suspend fun getSettingButton(mainStage: Stage): Deferred<JFXButton> = coroutineScope {
			async {
				JFXButton("设置").apply {
					setPrefSize(128.0, 46.0)
					onMousePressed = EventHandler {
						launchOnJavaFx {
							getSettingButtonAction(it, mainStage)
						}
					}
				}
			}
		}
		
		private suspend fun getSettingButtonAction(event: MouseEvent, mainStage: Stage) {
			if (event.isControlDown) {
				openSomething(wdtcCache)
			} else {
				tryCatching {
					SettingWindow.setSettingWin(mainStage)
				}
			}
		}
		
		private suspend fun launchGame(mainStage: Stage, version: Version) {
			val tasks = runOnIO {
				LaunchGame(version.noNull(), TaskWindow.taskPool).getTaskList { setWin(it, "启动失败") }
			}
			runOnJavaFx {
				TaskWindow(mainStage, tasks).run {
					showTaskStage()
				}
			}
			
		}
		
		
		suspend fun launchGameButtonTask(mainStage: Stage, version: Version?) {
			version.also {
				if (it != null) {
					if (isExistUserJsonFile) {
						launchGame(mainStage, it)
					} else {
						runOnJavaFx {
							noUser(mainStage)
						}
					}
				} else {
					GameVersionListWindow().setWindowScene(mainStage)
				}
			}
		}
		
		private fun noUser(mainStage: Stage) {
			NewUserWindows(mainStage).apply {
				title = "您还没有账户呢!"
				type = AccountsType.OFFLINE
			}.show()
		}
	}
}
