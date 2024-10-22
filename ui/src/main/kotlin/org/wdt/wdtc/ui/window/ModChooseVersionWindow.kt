package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.impl.download.fabric.FabricAPIDownloadTask
import org.wdt.wdtc.core.impl.download.fabric.FabricAPIVersionList
import org.wdt.wdtc.core.impl.download.fabric.FabricVersionImpl
import org.wdt.wdtc.core.impl.download.fabric.FabricVersionList
import org.wdt.wdtc.core.impl.download.forge.ForgeVersionImpl
import org.wdt.wdtc.core.impl.download.forge.ForgeVersionList
import org.wdt.wdtc.core.impl.download.quilt.QuiltVersionImpl
import org.wdt.wdtc.core.impl.download.quilt.QuiltVersionList
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.KindOfMod
import org.wdt.wdtc.core.openapi.manager.KindOfMod.*
import org.wdt.wdtc.core.openapi.utils.ioAsync

class ModChooseVersionWindow(
	private val kind: KindOfMod, private val mainStage: Stage, private val version: Version
) {
	private val size = mainStage.getSizeManger()
	
	private val modVersionList = ioAsync {
		when (kind) {
			FORGE -> {
				ForgeVersionList(version)
			}
			
			FABRICAPI -> {
				FabricAPIVersionList(version)
			}
			
			QUILT -> {
				QuiltVersionList(version)
			}
			
			else -> {
				FabricVersionList()
			}
		}.getVersionList()
	}
	
	
	fun setModChooser() {
		val back = JFXButton("返回").apply {
			styleClass.add("BlackBorder")
			onAction = eventHandler {
				ModChooseWindow(version, mainStage).run {
					setChooseWin()
				}
			}
		}
		val buttonList = VBox()
		val tips = Label("选择一个Mod版本:").apply {
			layoutX = 130.0
			layoutY = 20.0
		}
		tryCatching {
			launchOnJavaFx {
				modVersionList.await().forEach {
					size.modifyWindowsSize(buttonList, getVersionButton(it, buttonList))
				}
			}
		}
		val list = ScrollPane().apply {
			setTopAnchor(40.0)
			setRightAnchor(0.0)
			setLeftAnchor(0.0)
			setBottomAnchor(0.0)
			content = buttonList
		}
		AnchorPane().apply {
			background = wdtcBackground
			setStylesheets()
		}.let {
			size.modifyWindowsSize(it, list, back, tips)
			mainStage.setScene(Scene(it))
		}
	}
	
	private fun getVersionButton(versionJsonObject: VersionsJsonObjectInterface, buttonList: VBox): JFXButton {
		return JFXButton(versionJsonObject.versionNumber).apply {
			style = "-fx-border-color: #000000"
			prefWidth = 580.0
			onAction = eventHandler {
				when (kind) {
					FORGE -> ForgeVersionImpl(version, versionJsonObject)
					FABRIC -> FabricVersionImpl(version, versionJsonObject)
					QUILT -> QuiltVersionImpl(version, versionJsonObject)
					
					FABRICAPI -> (version as FabricVersionImpl).also {
						FabricAPIDownloadTask(it, versionJsonObject)
					}
					
					ORIGINAL -> version
					OTHER -> throw VersionNotFoundException("Can not be OTHER")
				}.let {
					ModChooseWindow(it, mainStage)
				}.setChooseWin()
				buttonList.children.clear()
			}
		}
	}
}
