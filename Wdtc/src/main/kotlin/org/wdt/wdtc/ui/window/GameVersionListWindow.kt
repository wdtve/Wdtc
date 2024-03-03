package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile.Companion.startDownloadVersionManifestJsonFile
import org.wdt.wdtc.core.download.game.GameVersionList
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.utils.ioCoroutineScope
import org.wdt.wdtc.core.utils.launch
import org.wdt.wdtc.core.utils.scwn

object GameVersionListWindow {
	fun setWindowScene(mainStage: Stage) {
		val versionList = ioCoroutineScope.run {
			launch("Download version manifest json") { downloadVersionManifestJsonFileTask() }
			async { GameVersionList().versionList }
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
			onAction = EventHandler {
				scwn("Download json") {
					startDownloadVersionManifestJsonFile()
				}
				setWindowScene(mainStage)
			}
		}
		val back = JFXButton("返回").apply {
			styleClass.add("BlackBorder")
			onAction = EventHandler {
				HomeWindow().run {
					setHome(mainStage)
				}
			}
		}
		val tips = Label("选择右侧的一个版本").apply {
			layoutX = 27.0
			layoutY = 71.0
		}
		javafxCoroutineScope.launch {
			versionList.await().forEach {
				val button = JFXButton(it.versionNumber).apply {
					prefWidth = 430.0
					styleClass.add("BlackBorder")
					onAction = EventHandler { _ ->
						Version(text).let { version ->
							if (isDebug) {
								ModChooseWindow(version, mainStage).run {
									setChooseWin()
								}
							} else {
								GameDownloadingWindow(version).run {
									setDownGameWin(mainStage)
								}
							}
						}
					}
				}
				size.modifyWindwosSize(list, button)
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
}
