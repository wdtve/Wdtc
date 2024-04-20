package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.currentVersionsList
import org.wdt.wdtc.core.openapi.game.preferredVersion
import org.wdt.wdtc.core.openapi.manger.GameDirectoryManger
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import org.wdt.wdtc.core.openapi.manger.changeSettingToFile
import org.wdt.wdtc.core.openapi.manger.currentSetting
import org.wdt.wdtc.core.openapi.utils.runOnIO

class VersionChooseWindow(private val path: GameDirectoryManger) {
	fun setWindow(mainStage: Stage) {
		val size = WindwosSizeManger(mainStage)
		val sonScrollPane = ScrollPane().apply {
			setLeftAnchor(100.0)
			setTopLowerRight()
			setTopAnchor(70.0)
		}
		val versionList = VBox()
		val back = JFXButton("返回").apply {
			onAction = eventHandler {
				((path as? Version)?.let { HomeWindow(it) } ?: HomeWindow()).run {
						setHome(mainStage)
				}
			}
		}
		launchOnJavaFx {
			currentVersionsList.forEach {
				val pane = Pane().apply {
					setPrefSize(514.0, 40.0)
				}
				val versionId = RadioButton(it.versionNumber).apply {
					if (preferredVersion != null && it == preferredVersion) {
						isSelected = true
					}
					layoutX = 14.0
					layoutY = 12.0
					onAction = eventHandler {
						runOnIO {
							currentSetting.changeSettingToFile {
									preferredVersion = it
							}
						}
						HomeWindow(it).run {
							setHome(mainStage)
						}
					}
				}
				val modKind = Label().apply {
					text = if (it.kind != KindOfMod.ORIGINAL) {
						"Mod : " + it.kind.toString()
					} else {
						"Mod : 无"
					}
					layoutX = 86.0
					layoutY = 13.0
				}
				size.modifyWindowsSize(pane, versionId, modKind)
				size.modifyWindowsSize(versionList, pane)
			}
		}
		versionList.let {
			it.setPrefSize(517.0, 416.0)
			sonScrollPane.content = it
		}
		val newGame = JFXButton("安装新游戏").apply {
			setPrefSize(100.0, 30.0)
			styleClass.add("BackGroundWriteButton")
			setTopLowerLeft()
			onAction = eventHandler { GameVersionListWindow().setWindowScene(mainStage) }
		}
		val tips = Label("\t当前目录").apply {
			prefWidth = 96.0
			layoutY = 70.0
		}
		AnchorPane().apply {
			children.addAll(newGame, back, sonScrollPane, tips)
			background = wdtcBackground
			setStylesheets()
		}.let {
			mainStage.setScene(Scene(it))
		}
		setCss("BlackBorder", back)
	}
}
