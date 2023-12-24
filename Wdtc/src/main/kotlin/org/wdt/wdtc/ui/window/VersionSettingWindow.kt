package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
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
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.InstallGameVersion
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.config.DefaultGameConfig
import org.wdt.wdtc.core.game.config.gameConfig
import org.wdt.wdtc.core.manger.putSettingToFile
import org.wdt.wdtc.core.manger.setting
import org.wdt.wdtc.core.utils.JavaUtils.getJavaVersion
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.modDownloadInfo
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

class VersionSettingWindow(private val launcher: Launcher, val mainStage: Stage) {
  private val config: DefaultGameConfig.Config = launcher.gameConfig.config!!
  private val size: WindwosSizeManger = mainStage.getSizeManger()
  private val LAYOUT_X = 10.0

  fun setWindow() {
    val window = HomeWindow(launcher)
    val parentPane = AnchorPane()
    val sonScrollPane = ScrollPane()
    sonScrollPane.layoutX = 105.0
    sonScrollPane.layoutY = 52.0
    AnchorPane.setTopAnchor(sonScrollPane, 50.0)
    AnchorPane.setLeftAnchor(sonScrollPane, 105.0)
    AnchorPane.setBottomAnchor(sonScrollPane, 0.0)
    AnchorPane.setRightAnchor(sonScrollPane, 0.0)
    val pane = AnchorPane()
    AnchorPane.setBottomAnchor(pane, 0.0)
    AnchorPane.setRightAnchor(pane, 0.0)
    AnchorPane.setLeftAnchor(pane, 0.0)
    AnchorPane.setTopAnchor(pane, 0.0)
    val back = JFXButton("返回")
    back.onAction = EventHandler { event: ActionEvent? -> window.setHome(mainStage) }
    val GameSetting = JFXButton("游戏设置")
    GameSetting.setPrefSize(105.0, 30.0)
    AnchorPane.setTopAnchor(GameSetting, 50.0)
    AnchorPane.setLeftAnchor(GameSetting, 0.0)
    val AutoDownload = JFXButton("自动下载")
    AutoDownload.isDisable = true
    AutoDownload.setPrefSize(105.0, 30.0)
    AnchorPane.setTopAnchor(AutoDownload, 80.0)
    AnchorPane.setLeftAnchor(AutoDownload, 0.0)
    GameSetting.isDisable = true
    setVersionSettingPane(sonScrollPane)
    GameSetting.onAction = EventHandler {
      AutoDownload.isDisable = false
      GameSetting.isDisable = true
      setVersionSettingPane(sonScrollPane)
    }
    AutoDownload.onAction = EventHandler {
      GameSetting.isDisable = false
      AutoDownload.isDisable = true
      setAutoDownload(sonScrollPane)
    }
    val completion = JFXButton("补全游戏文件")
    completion.layoutY = 395.0
    completion.setPrefSize(105.0, 30.0)
    AnchorPane.setBottomAnchor(completion, 30.0)
    AnchorPane.setLeftAnchor(completion, 0.0)
    val delete = JFXButton("删除该版本")
    delete.layoutY = 425.0
    delete.setPrefSize(105.0, 30.0)
    AnchorPane.setBottomAnchor(delete, 0.0)
    AnchorPane.setLeftAnchor(delete, 0.0)
    parentPane.children.addAll(sonScrollPane, completion, delete, back, GameSetting, AutoDownload)
    setCss("BlackBorder", back)
    parentPane.background = background
    parentPane.setStylesheets()
    mainStage.setScene(Scene(parentPane))
    setCss("BackGroundWriteButton", delete, completion, GameSetting, AutoDownload)
    delete.onAction = EventHandler {
      try {
        FileUtils.deleteDirectory(launcher.versionDirectory)
        setting.preferredVersion = null
        setting.putSettingToFile()
        val homeWindow = HomeWindow()
        homeWindow.setHome(mainStage)
        logmaker.info(launcher.versionNumber + " Deleted")
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
    completion.onAction = EventHandler {
      thread {
        val version = InstallGameVersion(launcher, true)
        version.startInstallGame()
        logmaker.info(launcher.versionNumber + " downloaded")
      }
    }
  }

  private fun setVersionSettingPane(scrollPane: ScrollPane) {
    val pane = AnchorPane()
    val line = 35.0
    val tips = Label("JDK地址:")
    tips.layoutX = LAYOUT_X
    tips.layoutY = line
    val tips2 = Label("版本:")
    tips2.layoutX = 107.0
    tips2.layoutY = line
    val line2 = 55.0
    val JavaPath = TextField()
    JavaPath.layoutX = LAYOUT_X
    JavaPath.layoutY = line2
    JavaPath.setPrefSize(300.0, 23.0)
    val choose = JFXButton("...")
    choose.layoutX = 315.0
    choose.layoutY = line2
    val tips3 = Label("游戏运行内存:")
    tips3.layoutX = LAYOUT_X
    tips3.layoutY = 89.0
    val Input = TextField()
    Input.layoutX = LAYOUT_X
    Input.layoutY = 104.0
    Input.setPrefSize(90.0, 23.0)
    val line3 = 138.0
    val tips4 = Label("窗口宽度:")
    tips4.layoutX = LAYOUT_X
    tips4.layoutY = line3
    val tips5 = Label("窗口高度:")
    tips5.layoutX = 165.0
    tips5.layoutY = line3
    val line4 = 156.0
    val InputWidth = TextField()
    InputWidth.layoutX = LAYOUT_X
    InputWidth.layoutY = line4
    InputWidth.setPrefSize(90.0, 23.0)
    val InputHeight = TextField()
    InputHeight.layoutX = 166.0
    InputHeight.layoutY = line4
    InputHeight.setPrefSize(90.0, 23.0)
    val tips6 = Label()
    tips6.layoutX = 340.0
    tips6.layoutY = 340.0
    tips6.setPrefSize(122.0, 15.0)
    val apply = JFXButton("应用")
    apply.setPrefSize(150.0, 50.0)
    AnchorPane.setBottomAnchor(apply, 10.0)
    AnchorPane.setRightAnchor(apply, 30.0)
    size.modifyWindwosSize(
      pane,
      tips,
      tips2,
      tips3,
      tips4,
      tips5,
      tips6,
      Input,
      JavaPath,
      InputHeight,
      InputWidth,
      choose
    )
    pane.children.add(apply)
    pane.setStylesheets()
    setCss("BlackBorder", choose, apply)
    scrollPane.content = pane
    JavaPath.text = config.javaPath
    InputWidth.text = config.width.toString()
    InputHeight.text = config.hight.toString()
    Input.text = config.memory.toString()
    tips2.text = "Java版本: ${getJavaVersion(config.javaPath)}"
    choose.onAction = EventHandler {
      val fileChooser = FileChooser()
      fileChooser.title = "选择Java文件"
      fileChooser.initialDirectory = File("C:\\Program Files")
      val javaExePath = fileChooser.showOpenDialog(mainStage)
      if (javaExePath != null) {
        JavaPath.text = javaExePath.absolutePath
      }
    }
    apply.onAction = EventHandler {
      try {
        if (File(JavaPath.text).isFileNotExists()) throw NumberFormatException()
        val newConfig =
          DefaultGameConfig.Config(Input.text.toInt(), JavaPath.text, InputWidth.text.toInt(), InputHeight.text.toInt())
        val gameConfig = launcher.gameConfig.defaultGameConfig
        gameConfig.config = newConfig
        logmaker.info(gameConfig)
        launcher.gameConfig.putConfigToFile(gameConfig)
        tips6.text = "设置成功"
        tips2.text = "Java版本: ${getJavaVersion(JavaPath.text)}"
      } catch (e: NumberFormatException) {
        tips6.textFill = Color.RED
        tips6.text = "请输入正确配置"
        logmaker.warn("配置无效", e)
      } catch (e: IOException) {
        tips6.textFill = Color.RED
        tips6.text = "请输入正确配置"
        logmaker.warn("配置无效", e)
      }
    }
  }

  private fun setAutoDownload(scrollPane: ScrollPane) {
    val modList = AnchorPane()
    var i = 0.0
    for (kind in KindOfMod.entries.toTypedArray()) {
      val modPane = AnchorPane()
      AnchorPane.setTopAnchor(modPane, 44 * i)
      modPane.prefHeight = 44.0
      modPane.prefWidth = 510.0
      setModPane(kind, modPane)
      size.modifyWindwosSize(modList, modPane)
      i++
    }
    modList.setStylesheets()
    scrollPane.content = modList
  }

  private fun setModPane(kind: KindOfMod, ModPane: AnchorPane) {
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
    AnchorPane.setTopAnchor(modIcon, 4.0)
    AnchorPane.setLeftAnchor(modIcon, 10.0)
    AnchorPane.setBottomAnchor(modIcon, 4.0)
    val modVersion = Label()
    if (launcher.kind == kind) {
      val info = launcher.modDownloadInfo
      modVersion.text = if (info != null) "$kind : ${info.modVersion}" else "$kind : 不安装"
    } else {
      modVersion.text = "$kind : 不安装"
    }
    AnchorPane.setBottomAnchor(modVersion, 15.0)
    AnchorPane.setLeftAnchor(modVersion, 60.0)
    AnchorPane.setTopAnchor(modVersion, 15.0)
    val download = JFXButton("-->")
    AnchorPane.setTopAnchor(download, 11.0)
    AnchorPane.setRightAnchor(download, 20.0)
    AnchorPane.setBottomAnchor(download, 11.0)
    ModPane.children.addAll(modIcon, modVersion, download)
  }

}
