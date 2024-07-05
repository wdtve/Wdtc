package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile.Companion.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile.Companion.startDownloadVersionManifestJsonFile
import org.wdt.wdtc.core.impl.download.game.GameVersionList
import org.wdt.wdtc.core.openapi.game.VersionImpl
import org.wdt.wdtc.core.openapi.manager.isDebug
import org.wdt.wdtc.core.openapi.utils.ioAsync
import org.wdt.wdtc.core.openapi.utils.runOnIO

class GameVersionListWindow {
	
	fun setWindowScene(mainStage: Stage) {
		val versionList = ioAsync {
			downloadVersionManifestJsonFileTask()
			GameVersionList().getVersionList()
		}
		val size = WindwosSizeManger(mainStage)
		val list = VBox().apply {
			setTopGrid()
		}
		val sp = ScrollPane().apply {
			setLeftAnchor(155.0)
			setTopAnchor(0.0)
			setBottomAnchor(0.0)
			setRightAnchor(0.0)
			content = list
			layoutX = 155.0
			prefHeight = windowsHeight
			prefWidth = 461.0
		}
		val refreshButton = JFXButton("刷新").apply {
			setPrefSize(155.0, 30.0)
			styleClass.add("BackGroundWriteButton")
			setBottomAnchor(0.0)
			setLeftAnchor(0.0)
			onAction = eventHandler(Dispatchers.Default) {
				runOnIO { startDownloadVersionManifestJsonFile() }
				runOnJavaFx { setWindowScene(mainStage) }
			}
		}
		val back = JFXButton("返回").apply {
			styleClass.add("BlackBorder")
			onAction = eventHandler {
				HomeWindow().run {
					setHome(mainStage)
				}
			}
		}
		val tips = Label("选择右侧的一个版本").apply {
			layoutX = 27.0
			layoutY = 71.0
		}
		launchOnJavaFx {
			versionList.await().forEach {
				JFXButton(it.versionNumber).apply {
					prefWidth = 430.0
					styleClass.add("BlackBorder")
					onAction = eventHandler {
						startDownload(text, mainStage)
					}
				}.also { button ->
					size.modifyWindowsSize(list, button)
				}
			}
		}
		val pane = AnchorPane().apply {
			children.addAll(sp, back, tips, refreshButton)
			setStylesheets()
			background = wdtcBackground
		}
		mainStage.apply {
			title = getWindowsTitle("下载游戏")
			scene = Scene(pane, 600.0, 450.0)
		}
	}
	
	private suspend fun startDownload(versionNumber: String, mainStage: Stage) {
		runOnJavaFx {
			VersionImpl(versionNumber).also {
				if (isDebug) {
					ModChooseWindow(it, mainStage).run {
						setChooseWin()
					}
				} else {
					GameDownloadingWindow(it).run {
						setDownGameWin(mainStage)
					}
				}
			}
		}
	}
}
