package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.sizeOfDirectory
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.*
import java.io.File
import java.io.IOException
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
			onAction = EventHandler {
				launchScope {
					HomeWindow().run {
						setHome(mainStage)
					}
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
			onAction = EventHandler {
				tipsField.text = tips
			}
		}
		
		val line = 55.0
		val gamePath = TextField().apply {
			text = currentSetting.defaultGamePath.canonicalPath
			layoutX = LAYOUT_X
			layoutY = line
			setPrefSize(297.0, 23.0)
		}
		val button = Button("...").apply {
			layoutX = 325.0
			layoutY = line
			onMousePressed = EventHandler { event: MouseEvent ->
				if (event.isControlDown) {
					openSomething(settingFile)
				} else {
					try {
						DirectoryChooser().apply {
							title = "选择游戏文件夹(设置后需要重启)"
							initialDirectory = currentSetting.defaultGamePath
						}.run {
							showDialog(mainStage)
						}.runIfNoNull {
							launchScope {
								withContext(Dispatchers.IO) {
									currentSetting.changeSettingToFile {
										defaultGamePath = this@runIfNoNull
									}
								}
								runOnJavaFx {
									logmaker.info("Game directory change:$canonicalPath")
									gamePath.text = canonicalPath
									Platform.exit()
								}
							}
						}
					} catch (e: IOException) {
						setErrorWin(e)
					}
				}
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
			onAction = EventHandler {
				trueLog.isSelected = false
				logmaker.info("启动日志器关闭显示")
				currentSetting.changeSettingToFile {
					console = false
				}
			}
		}
		trueLog.onAction = EventHandler {
			falseLog.isSelected = false
			logmaker.info("启动日志器开启显示")
			currentSetting.changeSettingToFile {
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
			logmaker.info("* Switch to Official DownloadSource")
			currentSetting.apply {
				downloadSource = DownloadSourceKind.OFFICIAL
			}
		}
		bmclDownloadSource.onAction = EventHandler {
			officialDownloadSource.isSelected = false
			mcbbsDownloadSource.isSelected = false
			logmaker.info("Switch to Bmcl DownloadSource")
			currentSetting.changeSettingToFile {
				downloadSource = DownloadSourceKind.BMCLAPI
			}
		}
		mcbbsDownloadSource.onAction = EventHandler {
			officialDownloadSource.isSelected = false
			bmclDownloadSource.isSelected = false
			logmaker.info("Switch to Mcbbs DownloadSource")
			currentSetting.changeSettingToFile {
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
			currentSetting.changeSettingToFile {
				llvmpipeLoader = true
			}
			logmaker.info("OpenGL软渲染已开启")
		}
		falseOpenGL.onAction = EventHandler {
			trueOpenGl.isSelected = false
			currentSetting.changeSettingToFile {
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
			logmaker.info("取消将游戏设置为中文")
			currentSetting.changeSettingToFile {
				chineseLanguage = false
			}
		}
		
		trueZhcn.onAction = EventHandler {
			falseZhcn.isSelected = false
			logmaker.info("将游戏设置为中文")
			currentSetting.changeSettingToFile {
				chineseLanguage = true
			}
		}
		
		val exportLog = JFXButton("导出日志").apply {
			layoutY = 420.0
			setLeftAnchor(0.0)
			setBottomAnchor(0.0)
			setPrefSize(105.0, 30.0)
			onAction = EventHandler {
				try {
					DirectoryChooser().apply {
						title = "选择日志文件保存路径"
						initialDirectory = wdtcConfig
					}.run {
						showDialog(mainStage)
					}.runIfNoNull {
						launchScope {
							val logFile = buildString {
								append("Wdtc-").append(launcherVersion).append("-")
								append(SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time))
								append(".log")
							}.let {
								File(canonicalPath, it)
							}.also {
								withContext(Dispatchers.IO) {
									FileUtils.copyFile(File(wdtcConfig, "logs/Wdtc.log"), it)
								}
							}
							logmaker.info("log file out:$logFile")
						}
					}
				} catch (e: IOException) {
					setErrorWin(e)
				}
			}
		}
		
		val cleanCache = JFXButton().apply {
			javafxScope.launch { text = "清除缓存:${wdtcCache.sizeOfDirectory()}B" }
			setPrefSize(105.0, 30.0)
			setLeftAnchor(0.0)
			setBottomAnchor(30.0)
			onMousePressed = EventHandler { event: MouseEvent ->
				if (event.isControlDown) {
					openSomething(wdtcCache)
				} else {
					try {
						launchScope {
							withContext(Dispatchers.IO) {
								FileUtils.cleanDirectory(wdtcCache)
								logmaker.info("Cache Folder Cleaned")
							}
							runOnJavaFx {
								text = "清除缓存:0B"
							}
						}
					} catch (e: IOException) {
						setErrorWin(e)
					}
				}
			}
		}
		
		val sonPane = AnchorPane().apply {
			setTopGrid()
			setPrefSize(493.0, 336.0)
		}.also {
			mainStage.getSizeManger().modifyWindwosSize(
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
		javafxScope.launch {
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
}
