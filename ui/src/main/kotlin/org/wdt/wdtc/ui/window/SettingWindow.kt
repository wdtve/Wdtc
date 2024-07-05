package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.sizeOfDirectory
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.info
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.openSomething
import org.wdt.wdtc.core.openapi.utils.runOnIO
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object SettingWindow {
	private const val LAYOUT_X = 20.0
	private const val LAYOUT_X_2 = 138.0
	
	suspend fun setSettingWin(mainStage: Stage) {
		runOnJavaFx {
			mainStage.title = getWindowsTitle("Setting")
		}
		val back = JFXButton("返回").apply {
			onAction = eventHandler {
				HomeWindow().run {
					setHome(mainStage)
				}
				logmaker.info(currentSetting)
			}
			styleClass.add("BlackBorder")
		}
		
		val tipsField = TextField().apply {
			setPrefSize(356.0, 23.0)
			setLeftAnchor(105.0)
			setTopAnchor(39.0)
			setRightAnchor(96.0)
			promptText = "Tips"
		}
		val next = JFXButton("下一个").apply {
			setPrefSize(96.0, 23.0)
			setRightAnchor(0.0)
			setTopAnchor(39.0)
			onAction = eventHandler {
				tipsField.text = getTips()
			}
		}
		
		val line = 55.0
		val gamePath = TextField().apply {
			text = currentSetting.defaultGamePath.canonicalPath
			layoutX = LAYOUT_X
			layoutY = line
			setPrefSize(297.0, 23.0)
		}
		val button = JFXButton("...").apply {
			layoutX = 325.0
			layoutY = line
			onMousePressed = EventHandler {
				launchOnJavaFx { chooseDirectoryButonAction(it, mainStage) }
			}
		}
		
		val line1 = 35.0
		val tips = Label("游戏文件夹位置:").apply {
			layoutX = LAYOUT_X
			layoutY = line1
		}
		val tips2 = Label("如:选择C盘则游戏文件夹为\"C:\\minceaft\"").apply {
			layoutX = 107.0
			layoutY = line1
			styleClass.add("tips")
		}
		val cmd = Label("启动时是否显示控制台窗口(如果按启动后长时间没反应可以设置显示,默认不显示):").apply {
			layoutX = LAYOUT_X
			layoutY = 89.0
		}
		val line2 = 111.0
		val trueLog = RadioButton("显示").apply {
			layoutX = LAYOUT_X
			layoutY = line2
		}
		val falseLog = RadioButton("不显示").apply {
			layoutX = LAYOUT_X_2
			layoutY = line2
			onAction = eventHandler {
				trueLog.isSelected = false
				currentSetting.saveSettingToFile {
					console = false
				}
			}
		}
		trueLog.onAction = eventHandler {
			falseLog.isSelected = false
			currentSetting.saveSettingToFile {
				console = true
			}
		}
		
		val line3 = 159.0
		val downloadSourceTips = Label("选择下载源(默认选择Official):").apply {
			layoutX = LAYOUT_X
			layoutY = 138.0
		}
		val officialDownloadSource = RadioButton("Official").apply {
			layoutX = LAYOUT_X
			layoutY = line3
		}
		val bmclDownloadSource = RadioButton("Bmcl").apply {
			layoutX = LAYOUT_X_2
			layoutY = line3
			
		}
		val mcbbsDownloadSource = RadioButton("Mcbbs").apply {
			layoutX = 281.0
			layoutY = line3
		}
		officialDownloadSource.onAction = EventHandler {
			bmclDownloadSource.isSelected = false
			mcbbsDownloadSource.isSelected = false
			currentSetting.saveSettingToFile {
				downloadSource = DownloadSourceKind.OFFICIAL
			}
		}
		bmclDownloadSource.onAction = eventHandler {
			officialDownloadSource.isSelected = false
			mcbbsDownloadSource.isSelected = false
			currentSetting.saveSettingToFile {
				downloadSource = DownloadSourceKind.BMCLAPI
			}
		}
		mcbbsDownloadSource.onAction = eventHandler {
			officialDownloadSource.isSelected = false
			bmclDownloadSource.isSelected = false
			currentSetting.saveSettingToFile {
				downloadSource = DownloadSourceKind.MCBBS
			}
		}
		
		val tips3 = Label("是否启用OpenGL软渲染器:").apply {
			layoutX = LAYOUT_X
			layoutY = 185.0
		}
		val line4 = 209.0
		val trueOpenGl = RadioButton("启用").apply {
			layoutX = LAYOUT_X
			layoutY = line4
		}
		val falseOpenGL = RadioButton("不启用").apply {
			layoutX = LAYOUT_X_2
			layoutY = line4
		}
		trueOpenGl.onAction = EventHandler {
			falseOpenGL.isSelected = false
			currentSetting.saveSettingToFile {
				llvmpipeLoader = true
			}
			logmaker.info("OpenGL软渲染已开启")
		}
		falseOpenGL.onAction = EventHandler {
			trueOpenGl.isSelected = false
			currentSetting.saveSettingToFile {
				llvmpipeLoader = false
			}
			logmaker.info("OpenGL软渲染已关闭")
		}
		
		val tips4 = Label("将游戏设置成中文(默认开启):").apply {
			layoutX = LAYOUT_X
			layoutY = 235.0
		}
		val line5 = 254.0
		val trueZhcn = RadioButton("启用").apply {
			layoutX = LAYOUT_X
			layoutY = line5
		}
		val falseZhcn = RadioButton("不启用").apply {
			layoutX = LAYOUT_X_2
			layoutY = line5
		}
		falseZhcn.onAction = EventHandler {
			trueZhcn.isSelected = false
			currentSetting.saveSettingToFile {
				chineseLanguage = false
			}
		}
		
		trueZhcn.onAction = EventHandler {
			falseZhcn.isSelected = false
			currentSetting.saveSettingToFile {
				chineseLanguage = true
			}
		}
		
		val exportLog = JFXButton("导出日志").apply {
			layoutY = 420.0
			setLeftAnchor(0.0)
			setBottomAnchor(0.0)
			setPrefSize(105.0, 30.0)
			onAction = eventHandler {
				tryCatching {
					val file = DirectoryChooser().apply {
						title = "选择日志文件保存路径"
						initialDirectory = wdtcConfig
					}.showDialog(mainStage)?.run {
						buildString {
							append("Wdtc-").append(launcherVersion).append("-")
							append(SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time))
							append(".log")
						}.let {
							File(canonicalPath, it)
						}
					}
					if (file != null) {
						runOnIO { FileUtils.copyFile(wdtcConfig.resolve("logs/Wdtc.log"), file) }
					}
				}
			}
		}
		
		
		val cleanCache = JFXButton().apply {
			launchOnJavaFx { text = "清除缓存:${wdtcCache.sizeOfDirectory()}B" }
			setPrefSize(105.0, 30.0)
			setLeftAnchor(0.0)
			setBottomAnchor(30.0)
			onMousePressed = EventHandler { launchOnJavaFx { cleanCacheAntion(it) } }
		}
		
		val sonPane = AnchorPane().apply {
			setTopGrid()
			setPrefSize(493.0, 336.0)
		}.also {
			mainStage.getSizeManger().modifyWindowsSize(
				it, officialDownloadSource, bmclDownloadSource, mcbbsDownloadSource, trueLog, falseLog,
				cmd, downloadSourceTips, gamePath, tips2, tips, button, tips3, trueOpenGl, falseOpenGL, tips4,
				trueZhcn, falseZhcn
			)
		}
		val scrollPane = ScrollPane(sonPane).apply {
			setLeftAnchor(105.0)
			setTopAnchor(70.0)
			setRightAnchor(0.0)
			setBottomAnchor(0.0)
		}
		AnchorPane().apply {
			setStylesheets()
			children.addAll(scrollPane, back, exportLog, cleanCache, tipsField, next)
			background = wdtcBackground
		}.let {
			runOnJavaFx {
				mainStage.scene = Scene(it)
			}
		}
		setCss("BackGroundWriteButton", exportLog, cleanCache, next)
		launchOnJavaFx {
			currentSetting.run {
				falseLog.isSelected = !console
				falseOpenGL.isSelected = !llvmpipeLoader
				falseZhcn.isSelected = !chineseLanguage
				trueLog.isSelected = console
				trueOpenGl.isSelected = llvmpipeLoader
				trueZhcn.isSelected = chineseLanguage
				when (downloadSource) {
					DownloadSourceKind.MCBBS -> mcbbsDownloadSource.isSelected = true
					DownloadSourceKind.BMCLAPI -> bmclDownloadSource.isSelected = true
					DownloadSourceKind.OFFICIAL -> officialDownloadSource.isSelected = true
				}
			}
		}
	}
	
	private suspend fun chooseDirectoryButonAction(event: MouseEvent, mainStage: Stage) {
		if (event.isControlDown) {
			openSomething(settingFile)
		} else {
			DirectoryChooser().apply {
				title = "选择游戏文件夹(设置后需要重启)"
				initialDirectory = currentSetting.defaultGamePath
			}.showDialog(mainStage)?.run {
				runOnIO {
					tryCatching {
						currentSetting.saveSettingToFile {
							defaultGamePath = this@run
						}
					}
					logmaker.info("Game directory change:${this@run.canonicalPath}")
				}
				runOnJavaFx {
					Platform.exit()
				}
			}
		}
		
	}
	
	private suspend fun JFXButton.cleanCacheAntion(event: MouseEvent) {
		if (event.isControlDown) {
			openSomething(wdtcCache)
		} else {
			runOnIO {
				tryCatching {
					FileUtils.cleanDirectory(wdtcCache)
					logmaker.info("Cache Folder Cleaned")
				}
			}
			runOnJavaFx {
				text = "清除缓存:0B"
			}
		}
		
	}
}
