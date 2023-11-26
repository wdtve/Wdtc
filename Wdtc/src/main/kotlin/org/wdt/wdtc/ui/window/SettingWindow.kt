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
import org.wdt.wdtc.core.manger.DownloadSourceManger.DownloadSourceList
import org.wdt.wdtc.core.manger.FileManger.settingFile
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.manger.FileManger.wdtcConfig
import org.wdt.wdtc.core.manger.SettingManger
import org.wdt.wdtc.core.utils.URLUtils.openSomething
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.Consoler.setTopGrid
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SettingWindow : SettingManger() {
  private val logmaker = getLogger(SettingWindow::class.java)

  @Throws(IOException::class)
  fun setSettingWin(MainStage: Stage) {
    val setting = setting
    val back = JFXButton("返回")
    back.onAction = EventHandler { event: ActionEvent? ->
      val win = HomeWindow()
      win.setHome(MainStage)
      logmaker.info(Companion.setting)
    }
    back.styleClass.add("BlackBorder")
    MainStage.title = Consoler.getWindowsTitle("Setting")
    val line = 55.0
    val gamePath = TextField()
    gamePath.text = setting.defaultGamePath.canonicalPath
    gamePath.layoutX = Coordinate.layoutX
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
          fileChooser.initialDirectory = Companion.setting.defaultGamePath
          val file = fileChooser.showDialog(MainStage)
          if (Objects.nonNull(file)) {
            setting.defaultGamePath = file
            putSettingToFile(setting)
            gamePath.text = file.getCanonicalPath()
            logmaker.info("* 游戏文件夹已更改为:$file")
          }
        } catch (e: IOException) {
          ExceptionWindow.setErrorWin(e)
        }
      }
    }
    val line1 = 35.0
    val tips = Label("游戏文件夹位置:")
    tips.layoutX = Coordinate.layoutX
    tips.layoutY = line1
    val tips2 = Label("如:选择C盘则游戏文件夹为\"C:\\minceaft\"")
    tips2.layoutX = 107.0
    tips2.layoutY = line1
    tips2.styleClass.add("tips")
    val cmd = Label("启动时是否显示控制台窗口(如果按启动后长时间没反应可以设置显示,默认不显示):")
    cmd.layoutX = Coordinate.layoutX
    cmd.layoutY = 89.0
    val TrueLog = RadioButton("显示")
    val FalseLog = RadioButton("不显示")
    val line2 = 111.0
    TrueLog.layoutX = Coordinate.layoutX
    TrueLog.layoutY = line2
    FalseLog.layoutX = Coordinate.layoutX2
    FalseLog.layoutY = line2
    FalseLog.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      TrueLog.isSelected = false
      logmaker.info("启动日志器关闭显示")
      setting.console = false
      putSettingToFile(setting)
    }
    TrueLog.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      FalseLog.isSelected = false
      logmaker.info("启动日志器开启显示")
      setting.console = true
      putSettingToFile(setting)
    }
    val DownloadSourceTips = Label("选择下载源(默认选择Official):")
    DownloadSourceTips.layoutX = Coordinate.layoutX
    DownloadSourceTips.layoutY = 138.0
    val OfficialDownloadSource = RadioButton("Official")
    val BmclDownloadSource = RadioButton("Bmcl")
    val McbbsDownloadSource = RadioButton("Mcbbs")
    val line3 = 159.0
    OfficialDownloadSource.layoutX = Coordinate.layoutX
    OfficialDownloadSource.layoutY = line3
    BmclDownloadSource.layoutX = Coordinate.layoutX2
    BmclDownloadSource.layoutY = line3
    McbbsDownloadSource.layoutX = 281.0
    McbbsDownloadSource.layoutY = line3
    OfficialDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      BmclDownloadSource.isSelected = false
      McbbsDownloadSource.isSelected = false
      logmaker.info("* Switch to Official DownloadSource")
      setting.downloadSource = DownloadSourceList.OFFICIAL
      putSettingToFile(setting)
    }
    BmclDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      OfficialDownloadSource.isSelected = false
      McbbsDownloadSource.isSelected = false
      logmaker.info("Switch to Bmcl DownloadSource")
      setting.downloadSource = DownloadSourceList.BMCLAPI
      putSettingToFile(setting)
    }
    McbbsDownloadSource.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      OfficialDownloadSource.isSelected = false
      BmclDownloadSource.isSelected = false
      logmaker.info("Switch to Mcbbs DownloadSource")
      setting.downloadSource = DownloadSourceList.MCBBS
      putSettingToFile(setting)
    }
    val tips3 = Label("是否启用OpenGL软渲染器:")
    tips3.layoutX = Coordinate.layoutX
    tips3.layoutY = 185.0
    val TrueOpenGl = RadioButton("启用")
    val FalseOpenGL = RadioButton("不启用")
    val line4 = 209.0
    TrueOpenGl.layoutX = Coordinate.layoutX
    TrueOpenGl.layoutY = line4
    FalseOpenGL.layoutX = Coordinate.layoutX2
    FalseOpenGL.layoutY = line4
    TrueOpenGl.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      FalseOpenGL.isSelected = false
      setting.llvmpipeLoader = true
      logmaker.info("OpenGL软渲染已开启")
      putSettingToFile(setting)
    }
    FalseOpenGL.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      TrueOpenGl.isSelected = false
      setting.llvmpipeLoader = false
      logmaker.info("OpenGL软渲染已关闭")
      putSettingToFile(setting)
    }
    val tips4 = Label("将游戏设置成中文(默认开启):")
    tips4.layoutX = Coordinate.layoutX
    tips4.layoutY = 235.0
    val TrueZhcn = RadioButton("启用")
    val line5 = 254.0
    TrueZhcn.layoutX = Coordinate.layoutX
    TrueZhcn.layoutY = line5
    val FalseZhcn = RadioButton("不启用")
    FalseZhcn.layoutX = Coordinate.layoutX2
    FalseZhcn.layoutY = line5
    FalseZhcn.onAction = EventHandler {
      setting.chineseLanguage = false
      TrueZhcn.isSelected = false
      logmaker.info("取消将游戏设置为中文")
      putSettingToFile(setting)
    }
    TrueZhcn.onAction = EventHandler {
      setting.chineseLanguage = true
      FalseZhcn.isSelected = false
      logmaker.info("将游戏设置为中文")
      putSettingToFile(setting)
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
        val logDirectory = fileChooser.showDialog(MainStage)
        if (Objects.nonNull(logDirectory)) {
          val calendar = Calendar.getInstance()
          val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
          val srcFile = File(wdtcCache, "logs/Wdtc.log")
          val logFile = File(
            logDirectory.getAbsoluteFile(),
            "Wdtc-Demo-" + formatter.format(calendar.time) + ".log"
          )
          FileUtils.copyFile(srcFile, logFile)
          logmaker.info("日志已导出:$logFile")
        }
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
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
          cleanCache.text = "清除缓存:" + FileUtils.sizeOfDirectory(wdtcCache) + "B"
        } catch (e: IOException) {
          logmaker.error("Clean Cache Folder Error,", e)
        }
      }
    }
    val SonPane = AnchorPane()
    SonPane.setTopGrid()
    SonPane.setPrefSize(493.0, 336.0)
    val size = WindwosSizeManger(MainStage)
    size.modifyWindwosSize(
      SonPane,
      back,
      OfficialDownloadSource,
      BmclDownloadSource,
      McbbsDownloadSource,
      TrueLog,
      FalseLog,
      cmd,
      DownloadSourceTips,
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
    val scrollPane = ScrollPane(SonPane)
    AnchorPane.setLeftAnchor(scrollPane, 105.0)
    AnchorPane.setTopAnchor(scrollPane, 70.0)
    AnchorPane.setRightAnchor(scrollPane, 0.0)
    AnchorPane.setBottomAnchor(scrollPane, 0.0)
    val pane = AnchorPane()
    Consoler.setCss("BackGroundWriteButton", ExportLog, cleanCache)
    pane.setStylesheets()
    pane.children.addAll(scrollPane, back, ExportLog, cleanCache)
    pane.background = Consoler.background
    MainStage.setScene(Scene(pane))
    FalseLog.isSelected = !setting.console
    FalseOpenGL.isSelected = !setting.llvmpipeLoader
    FalseZhcn.isSelected = !setting.chineseLanguage
    TrueLog.isSelected = setting.console
    TrueOpenGl.isSelected = setting.llvmpipeLoader
    TrueZhcn.isSelected = setting.chineseLanguage
    when (setting.downloadSource) {
      DownloadSourceList.MCBBS -> McbbsDownloadSource.isSelected = true
      DownloadSourceList.BMCLAPI -> BmclDownloadSource.isSelected = true
      DownloadSourceList.OFFICIAL -> OfficialDownloadSource.isSelected = true
    }
  }

  private object Coordinate {
    const val layoutX = 20.0
    const val layoutX2 = 138.0
  }
}
