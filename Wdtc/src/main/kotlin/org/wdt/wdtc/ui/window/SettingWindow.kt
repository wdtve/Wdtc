package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.sizeOfDirectory
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.openSomething
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SettingWindow {
  private const val LAYOUT_X = 20.0
  private const val LAYOUT_X_2 = 138.0

  @Throws(IOException::class)
  fun setSettingWin(mainStage: Stage) {
    val back = JFXButton("返回")
    back.onAction = EventHandler {
      val win = HomeWindow()
      win.setHome(mainStage)
      logmaker.info(setting)
    }
    back.styleClass.add("BlackBorder")
    mainStage.title = getWindowsTitle("Setting")
    val line = 55.0
    val gamePath = TextField()
    gamePath.text = setting.defaultGamePath.canonicalPath
    gamePath.layoutX = LAYOUT_X
    gamePath.layoutY = line
    gamePath.setPrefSize(297.0, 23.0)
    val button = Button("...")
    button.layoutX = 325.0
    button.layoutY = line
    button.onMousePressed = EventHandler { event: MouseEvent ->
      if (event.isControlDown) {
        openSomething(settingFile)
      } else {
        try {
          val fileChooser = DirectoryChooser()
          fileChooser.title = "选择游戏文件夹"
          fileChooser.initialDirectory = setting.defaultGamePath
          val file = fileChooser.showDialog(mainStage)
          if (Objects.nonNull(file)) {
            setting.defaultGamePath = file
            setting.putSettingToFile()
            gamePath.text = file.canonicalPath
            logmaker.info("* 游戏文件夹已更改为:$file")
          }
        } catch (e: IOException) {
          setErrorWin(e)
        }
      }
    }
    val line1 = 35.0
    val tips = Label("游戏文件夹位置:")
    tips.layoutX = LAYOUT_X
    tips.layoutY = line1
    val tips2 = Label("如:选择C盘则游戏文件夹为\"C:\\minceaft\"")
    tips2.layoutX = 107.0
    tips2.layoutY = line1
    tips2.styleClass.add("tips")
    val cmd = Label("启动时是否显示控制台窗口(如果按启动后长时间没反应可以设置显示,默认不显示):")
    cmd.layoutX = LAYOUT_X
    cmd.layoutY = 89.0
    val trueLog = RadioButton("显示")
    val falseLog = RadioButton("不显示")
    val line2 = 111.0
    trueLog.layoutX = LAYOUT_X
    trueLog.layoutY = line2
    falseLog.layoutX = LAYOUT_X_2
    falseLog.layoutY = line2
    falseLog.onAction = EventHandler {
      trueLog.isSelected = false
      logmaker.info("启动日志器关闭显示")
      setting.console = false
      setting.putSettingToFile()
    }
    trueLog.onAction = EventHandler {
      falseLog.isSelected = false
      logmaker.info("启动日志器开启显示")
      setting.console = true
      setting.putSettingToFile()
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
    officialDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      bmclDownloadSource.isSelected = false
      mcbbsDownloadSource.isSelected = false
      logmaker.info("* Switch to Official DownloadSource")
      setting.downloadSource = DownloadSourceList.OFFICIAL
      setting.putSettingToFile()
    }
    bmclDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      officialDownloadSource.isSelected = false
      mcbbsDownloadSource.isSelected = false
      logmaker.info("Switch to Bmcl DownloadSource")
      setting.downloadSource = DownloadSourceList.BMCLAPI
      setting.putSettingToFile()
    }
    mcbbsDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      officialDownloadSource.isSelected = false
      bmclDownloadSource.isSelected = false
      logmaker.info("Switch to Mcbbs DownloadSource")
      setting.downloadSource = DownloadSourceList.MCBBS
      setting.putSettingToFile()
    }
    val tips3 = Label("是否启用OpenGL软渲染器:")
    tips3.layoutX = LAYOUT_X
    tips3.layoutY = 185.0
    val TrueOpenGl = RadioButton("启用")
    val FalseOpenGL = RadioButton("不启用")
    val line4 = 209.0
    TrueOpenGl.layoutX = LAYOUT_X
    TrueOpenGl.layoutY = line4
    FalseOpenGL.layoutX = LAYOUT_X_2
    FalseOpenGL.layoutY = line4
    TrueOpenGl.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      FalseOpenGL.isSelected = false
      setting.llvmpipeLoader = true
      logmaker.info("OpenGL软渲染已开启")
      setting.putSettingToFile()
    }
    FalseOpenGL.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      TrueOpenGl.isSelected = false
      setting.llvmpipeLoader = false
      logmaker.info("OpenGL软渲染已关闭")
      setting.putSettingToFile()
    }
    val tips4 = Label("将游戏设置成中文(默认开启):")
    tips4.layoutX = LAYOUT_X
    tips4.layoutY = 235.0
    val TrueZhcn = RadioButton("启用")
    val line5 = 254.0
    TrueZhcn.layoutX = LAYOUT_X
    TrueZhcn.layoutY = line5
    val FalseZhcn = RadioButton("不启用")
    FalseZhcn.layoutX = LAYOUT_X_2
    FalseZhcn.layoutY = line5
    FalseZhcn.onAction = EventHandler {
      setting.chineseLanguage = false
      TrueZhcn.isSelected = false
      logmaker.info("取消将游戏设置为中文")
      setting.putSettingToFile()
    }
    TrueZhcn.onAction = EventHandler {
      setting.chineseLanguage = true
      FalseZhcn.isSelected = false
      logmaker.info("将游戏设置为中文")
      setting.putSettingToFile()
    }
    val ExportLog = JFXButton("导出日志")
    ExportLog.layoutY = 420.0
    AnchorPane.setLeftAnchor(ExportLog, 0.0)
    AnchorPane.setBottomAnchor(ExportLog, 0.0)
    ExportLog.setPrefSize(105.0, 30.0)
    ExportLog.onAction = EventHandler {
      try {
        val fileChooser = DirectoryChooser()
        fileChooser.title = "选择日志文件保存路径"
        fileChooser.initialDirectory = wdtcConfig
        val logDirectory = fileChooser.showDialog(mainStage)
        if (logDirectory != null) {
          val calendar = Calendar.getInstance()
          val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
          val srcFile = File(wdtcCache, "logs/Wdtc.log")
          val logFile = File(
            logDirectory.canonicalPath,
            "Wdtc-Demo-" + formatter.format(calendar.time) + ".log"
          )
          FileUtils.copyFile(srcFile, logFile)
          logmaker.info("日志已导出:$logFile")
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
        logmaker.info("缓存文件夹已打开")
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
      TrueOpenGl,
      FalseOpenGL,
      tips4,
      TrueZhcn,
      FalseZhcn
    )
    val scrollPane = ScrollPane(sonPane)
    AnchorPane.setLeftAnchor(scrollPane, 105.0)
    AnchorPane.setTopAnchor(scrollPane, 70.0)
    AnchorPane.setRightAnchor(scrollPane, 0.0)
    AnchorPane.setBottomAnchor(scrollPane, 0.0)
    val pane = AnchorPane()
    setCss("BackGroundWriteButton", ExportLog, cleanCache)
    pane.setStylesheets()
    pane.children.addAll(scrollPane, back, ExportLog, cleanCache)
    pane.background = background
    mainStage.setScene(Scene(pane))
    falseLog.isSelected = !setting.console
    FalseOpenGL.isSelected = !setting.llvmpipeLoader
    FalseZhcn.isSelected = !setting.chineseLanguage
    trueLog.isSelected = setting.console
    TrueOpenGl.isSelected = setting.llvmpipeLoader
    TrueZhcn.isSelected = setting.chineseLanguage
    when (setting.downloadSource) {
      DownloadSourceList.MCBBS -> mcbbsDownloadSource.isSelected = true
      DownloadSourceList.BMCLAPI -> bmclDownloadSource.isSelected = true
      DownloadSourceList.OFFICIAL -> officialDownloadSource.isSelected = true
    }
  }

}
