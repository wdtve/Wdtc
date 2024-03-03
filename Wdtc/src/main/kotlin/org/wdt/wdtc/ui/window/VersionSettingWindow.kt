package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.game.GameConfig
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.VersionsList.Companion.changeListToFile
import org.wdt.wdtc.core.game.currentVersionsList
import org.wdt.wdtc.core.manger.changeSettingToFile
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.gameConfig
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.utils.*
import org.wdt.wdtc.core.utils.JavaUtils.getJavaVersion
import java.io.File
import java.io.IOException
import java.util.*

class VersionSettingWindow(private val version: Version, val mainStage: Stage) {
	private val config: GameConfig.Config = version.gameConfig.config
	private val size: WindwosSizeManger = mainStage.getSizeManger()
	private val javaVersion = ioCoroutineScope.async { getJavaVersion(config.javaPath) }
	
	fun setWindow() {
		val window = HomeWindow(version)
		val sonScrollPane = ScrollPane().apply {
			layoutX = 105.0
			layoutY = 52.0
			setTopAnchor(50.0)
			setLeftAnchor(105.0)
			setBottomAnchor(0.0)
			setRightAnchor(0.0)
		}
		val back = JFXButton("返回").apply {
			onAction = EventHandler { window.setHome(mainStage) }
		}
		val gameSetting = JFXButton("游戏设置").apply {
			setPrefSize(105.0, 30.0)
			setTopAnchor(50.0)
			setLeftAnchor(0.0)
			isDisable = true
		}
		val autoDownload = JFXButton("自动下载").apply {
			isDisable = !isDebug
			setPrefSize(105.0, 30.0)
			setTopAnchor(80.0)
			setLeftAnchor(0.0)
		}
		setVersionSettingPane(sonScrollPane)
		gameSetting.onAction = EventHandler {
			autoDownload.isDisable = false
			gameSetting.isDisable = true
			setVersionSettingPane(sonScrollPane)
		}
		autoDownload.onAction = EventHandler {
			gameSetting.isDisable = false
			autoDownload.isDisable = true
			setAutoDownload(sonScrollPane)
		}
		val completion = JFXButton("补全游戏文件").apply {
			layoutY = 395.0
			setPrefSize(105.0, 30.0)
			setBottomAnchor(30.0)
			setLeftAnchor(0.0)
			onAction = EventHandler {
				javafxCoroutineScope.launch("completion ${version.versionNumber} task") {
					isDisable = true
					back.isDisable = true
					val completionJob = launch(Dispatchers.Default) {
						InstallGameVersion(version, false).run {
							startInstallGame()
						}
					}
					launch(Dispatchers.JavaFx) {
						completionJob.join()
						isDisable = false
						back.isDisable = false
						logmaker.info("${version.versionNumber} downloaded")
					}
				}
			}
			
		}
		val delete = JFXButton("删除该版本").apply {
			layoutY = 425.0
			setPrefSize(105.0, 30.0)
			setBottomAnchor(0.0)
			setLeftAnchor(0.0)
			onAction = EventHandler {
				try {
					scwn("remove version") {
						launch {
							FileUtils.deleteDirectory(version.versionDirectory)
							currentSetting.changeSettingToFile {
								preferredVersion = null
							}
							logmaker.info(version.versionNumber + " Deleted")
						}
						launch {
							currentVersionsList.changeListToFile {
								remove(version)
							}
						}
						launch(Dispatchers.JavaFx) {
							HomeWindow().run {
								setHome(mainStage)
							}
						}
					}
				} catch (e: IOException) {
					setErrorWin(e)
				}
			}
		}
		setCss("BlackBorder", back)
		AnchorPane().apply {
			children.addAll(sonScrollPane, completion, delete, back, gameSetting, autoDownload)
			background = wdtcBackground
			setStylesheets()
			mainStage.setScene(Scene(this))
		}
		setCss("BackGroundWriteButton", delete, completion, gameSetting, autoDownload)
		
	}
	
	private fun setVersionSettingPane(scrollPane: ScrollPane) {
		val line = 35.0
		val tips = Label("JDK地址:").apply {
			layoutX = LAYOUT_X
			layoutY = line
		}
		val tips2 = Label("版本:").apply {
			layoutX = 107.0
			layoutY = line
		}
		val line2 = 55.0
		val javaPath = TextField().apply {
			layoutX = LAYOUT_X
			layoutY = line2
			setPrefSize(300.0, 23.0)
		}
		val choose = JFXButton("...").apply {
			layoutX = 315.0
			layoutY = line2
		}
		val tips3 = Label("游戏运行内存:").apply {
			layoutX = LAYOUT_X
			layoutY = 89.0
		}
		val input = TextField().apply {
			layoutX = LAYOUT_X
			layoutY = 104.0
			setPrefSize(90.0, 23.0)
		}
		val line3 = 138.0
		val tips4 = Label("窗口宽度:").apply {
			layoutX = LAYOUT_X
			layoutY = line3
		}
		val tips5 = Label("窗口高度:").apply {
			layoutX = 165.0
			layoutY = line3
		}
		val line4 = 156.0
		val inputWidth = TextField().apply {
			layoutX = LAYOUT_X
			layoutY = line4
			setPrefSize(90.0, 23.0)
		}
		val inputHeight = TextField().apply {
			layoutX = 166.0
			layoutY = line4
			setPrefSize(90.0, 23.0)
		}
		val tips6 = Label().apply {
			layoutX = 340.0
			layoutY = 340.0
			setPrefSize(122.0, 15.0)
		}
		val apply = JFXButton("应用").apply {
			setPrefSize(150.0, 50.0)
			setBottomAnchor(10.0)
			setRightAnchor(30.0)
		}
		val pane = AnchorPane().apply {
			children.add(apply)
			setStylesheets()
		}
		size.modifyWindwosSize(
			pane,
			tips,
			tips2,
			tips3,
			tips4,
			tips5,
			tips6,
			input,
			javaPath,
			inputHeight,
			inputWidth,
			choose
		)
		setCss("BlackBorder", choose, apply)
		scrollPane.content = pane
		javafxCoroutineScope.launch {
			config.run {
				javaPath.text = this.javaPath
				inputWidth.text = width.toString()
				inputHeight.text = hight.toString()
				input.text = memory.toString()
				tips2.text = "Java版本: ${javaVersion.await()}"
			}
		}
		choose.onAction = EventHandler {
			val javaExePath = FileChooser().apply {
				title = "选择Java文件"
				initialDirectory = File("C:\\Program Files")
			}.run {
				showOpenDialog(mainStage)
			}
			if (javaExePath != null) {
				javaPath.text = javaExePath.canonicalPath
			}
		}
		fun invalidConfiguration(e: Throwable) {
			tips6.textFill = Color.RED
			tips6.text = "请输入正确配置"
			logmaker.warning("配置无效", e)
		}
		apply.onAction = EventHandler {
			scwn {
				try {
					val javaVersion = getJavaVersionNumber(javaPath.text)
					val job = launch {
						version.gameConfig.run {
							this.configFileObject.apply {
								config = GameConfig.Config(
									input.text.toInt(), javaPath.text,
									inputWidth.text.toInt(), inputHeight.text.toInt()
								)
							}.let {
								logmaker.info(it)
								scwn {
									putConfigToFile(it)
								}
							}
						}
					}
					launch(Dispatchers.JavaFx) {
						job.join()
						tips6.text = "设置成功"
					}
					launch(Dispatchers.JavaFx) {
						javaVersion.await().noNull().let {
							tips2.text = "Java版本: $it"
						}
					}
				} catch (e: NumberFormatException) {
					invalidConfiguration(e)
				} catch (e: IOException) {
					invalidConfiguration(e)
				}
			}
		}
	}
	
	private fun CoroutineScope.getJavaVersionNumber(it: String): Deferred<String?> {
		return async {
			if (it.toFile().isFileNotExists())
				throw NumberFormatException()
			else
				getJavaVersion(it)
		}
	}
	
	private fun setAutoDownload(scrollPane: ScrollPane) {
		val modList = AnchorPane()
		var i = 0.0
		for (kind in KindOfMod.entries.toTypedArray()) {
			AnchorPane().apply {
				setTopAnchor(44 * i)
				prefHeight = 44.0
				prefWidth = 510.0
				setModPane(kind, this)
				size.modifyWindwosSize(modList, this)
				i++
			}
		}
		modList.let {
			it.setStylesheets()
			scrollPane.content = it
		}
	}
	
	private fun setModPane(kind: KindOfMod, modPane: AnchorPane) {
		val modIcon = ImageView()
		when (kind) {
			KindOfMod.FORGE -> modIcon.image = Image(
				Objects.requireNonNull(
					VersionSettingWindow::class.java.getResourceAsStream("/assets/icon/forge.png")
				)
			)
			
			KindOfMod.FABRIC -> modIcon.image = Image(
				Objects.requireNonNull(
					VersionSettingWindow::class.java.getResourceAsStream("/assets/icon/fabric.png")
				)
			)
			
			KindOfMod.QUILT -> modIcon.image = Image(
				Objects.requireNonNull(
					VersionSettingWindow::class.java.getResourceAsStream("/assets/icon/quilt.png")
				)
			)
			
			KindOfMod.ORIGINAL -> modIcon.image = Image(
				Objects.requireNonNull(
					VersionSettingWindow::class.java.getResourceAsStream("/assets/icon/ico.jpg")
				)
			)
			
			else -> {}
		}
		modIcon.apply {
			setTopAnchor(4.0)
			setLeftAnchor(10.0)
			setBottomAnchor(4.0)
		}
		val modVersion = Label().apply {
			if (version.kind == kind) {
				val info = version.modDownloadInfo
				text = if (info != null) "$kind : ${info.modVersion}" else "$kind : 不安装"
			} else {
				text = "$kind : 不安装"
			}
			setBottomAnchor(15.0)
			setLeftAnchor(60.0)
			setTopAnchor(15.0)
		}
		val download = JFXButton("-->").apply {
			setTopAnchor(11.0)
			setRightAnchor(20.0)
			setBottomAnchor(11.0)
		}
		modPane.children.addAll(modIcon, modVersion, download)
	}
	
	companion object {
		const val LAYOUT_X = 10.0
		
	}
	
}
