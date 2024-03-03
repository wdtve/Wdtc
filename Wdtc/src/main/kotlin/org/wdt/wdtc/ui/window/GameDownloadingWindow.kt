package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.downloadSourceKind
import org.wdt.wdtc.core.utils.openSomething
import org.wdt.wdtc.core.utils.scwn

class GameDownloadingWindow(private val version: Version) {
	fun setDownGameWin(mainStage: Stage) {
		val size = mainStage.run {
			title = getWindowsTitle("下载游戏")
			WindwosSizeManger(mainStage)
		}
		
		val time = Label("下载时间不会太长").apply {
			layoutX = 240.0
			layoutY = 160.0
		}
		val statusBar = Label("下面是状态栏").apply {
			layoutX = 253.0
			layoutY = 305.0
		}
		val bmclHome = Button("BMCLAPI").apply {
			onAction = EventHandler { openSomething("https://bmclapidoc.bangbang93.com/") }
			setRightAnchor(0.0)
			setTopAnchor(0.0)
		}
		val openBmcl = Label("国内快速下载源→").apply {
			setRightAnchor(70.0)
			setTopAnchor(4.0)
		}
		val textField = TextField().apply {
			setTopAnchor(350.0)
			setLeftAnchor(150.0)
			setRightAnchor(150.0)
			text = "${version.versionNumber} 开始下载,下载源: $downloadSourceKind"
		}
		val job = scwn("Install ${version.versionNumber} task") {
			InstallGameVersion(version, true) {
				textField.text = it
			}.run {
				startInstallGame()
			}
		}
		val back = JFXButton("返回").apply {
			onAction = EventHandler {
				scwn {
					launch(Dispatchers.JavaFx) {
						HomeWindow().run {
							setHome(mainStage)
						}
					}
					job.cancel()
				}
			}
			style = "-fx-border-color: #000000"
		}
		
		AnchorPane().apply {
			setPrefSize(mainStage.width, mainStage.height)
			this.background = wdtcBackground
		}.run {
			size.modifyWindwosSize(this, back, time, statusBar, bmclHome, openBmcl, textField)
			Scene(this).let {
				mainStage.scene = it
			}
		}
	}
}
