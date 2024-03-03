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
import org.wdt.utils.io.*
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SettingWindow {
	private const val LAYOUT_X = 20.0
	private const val LAYOUT_X_2 = 138.0
	
	@Throws(IOException::class)
	fun setSettingWin(mainStage: Stage) {
		mainStage.title = getWindowsTitle("Setting")
		val back = JFXButton("返回").apply {
			onAction = EventHandler {
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
						val fileChooser = DirectoryChooser().apply {
							title = "选择游戏文件夹(设置后需要重启)"
							initialDirectory = currentSetting.defaultGamePath
						}
						fileChooser.showDialog(mainStage).runIfNoNull {
							currentSetting.changeSettingToFile {
								defaultGamePath = this@runIfNoNull
							}
							gamePath.text = canonicalPath
							logmaker.info("Game directory change:$canonicalPath")
							Platform.exit()
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
		val downloadSourceTips = Label("选择下载源(默认选择Official):")
		downloadSourceTips.layoutX = LAYOUT_X
		downloadSourceTips.layoutY = 138.0
		val officialDownloadSource = RadioButton("Official")
		val bmclDownloadSource = RadioButton("Bmcl")
		val mcbbsDownloadSource = RadioButton("Mcbbs")
		val line3 = 159.0
		officialDownloadSource.layoutX = LAYOUT_X
		officialDownloadSource.layoutY = line3
		bmclDownloadSource.layoutX = LAYOUT_X_2
		bmclDownloadSource.layoutY = line3
		mcbbsDownloadSource.layoutX = 281.0
		mcbbsDownloadSource.layoutY = line3
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
		val tips3 = Label("是否启用OpenGL软渲染器:")
		tips3.layoutX = LAYOUT_X
		tips3.layoutY = 185.0
		val trueOpenGl = RadioButton("启用")
		val falseOpenGL = RadioButton("不启用")
		val line4 = 209.0
		trueOpenGl.layoutX = LAYOUT_X
		trueOpenGl.layoutY = line4
		falseOpenGL.layoutX = LAYOUT_X_2
		falseOpenGL.layoutY = line4
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
		val tips4 = Label("将游戏设置成中文(默认开启):")
		tips4.layoutX = LAYOUT_X
		tips4.layoutY = 235.0
		val trueZhcn = RadioButton("启用")
		val line5 = 254.0
		trueZhcn.layoutX = LAYOUT_X
		trueZhcn.layoutY = line5
		val falseZhcn = RadioButton("不启用")
		falseZhcn.layoutX = LAYOUT_X_2
		falseZhcn.layoutY = line5
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
		val exportLog = JFXButton("导出日志")
		exportLog.layoutY = 420.0
		AnchorPane.setLeftAnchor(exportLog, 0.0)
		AnchorPane.setBottomAnchor(exportLog, 0.0)
		exportLog.setPrefSize(105.0, 30.0)
		exportLog.onAction = EventHandler {
			try {
				DirectoryChooser().apply {
					title = "选择日志文件保存路径"
					initialDirectory = wdtcConfig
				}.run {
					showDialog(mainStage).runIfNoNull {
						val srcFile = File(wdtcCache, "logs/Wdtc.log")
						val logFile = File(
							canonicalPath,
							"Wdtc-Demo-${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().time)}.log"
						)
						FileUtils.copyFile(srcFile, logFile)
						logmaker.info("日志已导出:$logFile")
					}
				}
			} catch (e: IOException) {
				setErrorWin(e)
			}
		}
		
		val cleanCache = JFXButton()
		cleanCache.text = "清除缓存:${wdtcCache.sizeOfDirectory()}B"
		cleanCache.setPrefSize(105.0, 30.0)
		AnchorPane.setLeftAnchor(cleanCache, 0.0)
		AnchorPane.setBottomAnchor(cleanCache, 30.0)
		cleanCache.onMousePressed = EventHandler { event: MouseEvent ->
			if (event.isControlDown) {
				openSomething(wdtcCache)
			} else {
				try {
					FileUtils.cleanDirectory(wdtcCache)
					logmaker.info("Cache Folder Cleaned")
					cleanCache.text = "清除缓存:${wdtcCache.sizeOfDirectory()}B"
				} catch (e: IOException) {
					logmaker.error("Clean Cache Folder Error,", e)
				}
			}
		}
		
		val sonPane = AnchorPane()
		sonPane.setTopGrid()
		sonPane.setPrefSize(493.0, 336.0)
		val size = WindwosSizeManger(mainStage)
		size.modifyWindwosSize(
			sonPane,
			back,
			officialDownloadSource,
			bmclDownloadSource,
			mcbbsDownloadSource,
			trueLog,
			falseLog,
			cmd,
			downloadSourceTips,
			gamePath,
			tips2,
			tips,
			button,
			tips3,
			trueOpenGl,
			falseOpenGL,
			tips4,
			trueZhcn,
			falseZhcn
		)
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
			mainStage.setScene(Scene(it))
		}
		setCss("BackGroundWriteButton", exportLog, cleanCache, next)
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
