package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.VMManger.isDebug
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.ModUtils.gameModIsFabric
import org.wdt.wdtc.core.utils.ModUtils.gameModIsForge
import org.wdt.wdtc.core.utils.ModUtils.gameModIsQuilt
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import java.io.IOException

class ModChooseWindow(private val launcher: Launcher, private val MainStage: Stage) {
  private val size: WindwosSizeManger

  init {
    size = WindwosSizeManger(MainStage)
  }

  fun setChooseWin() {
    val pane = Pane()
    val back = JFXButton("返回")
    val title = Label(launcher.versionNumber)
    title.layoutX = 283.0
    title.layoutY = 69.0
    val forge = Label(Tips.ForgeNo)
    val fabric = Label(Tips.FabricNo)
    val fabricAPI = Label(Tips.FabricAPINo)
    val quilt = Label(Tips.QuiltNo)
    val forgePane = Pane()
    forgePane.layoutX = 60.0
    forgePane.layoutY = 96.0
    forgePane.setPrefSize(160.0, 90.0)
    forge.layoutX = 36.0
    forge.layoutY = 27.0
    val downloadForge = JFXButton("->")
    downloadForge.layoutX = 67.0
    downloadForge.layoutY = 53.0
    downloadForge.setPrefSize(47.0, 23.0)
    downloadForge.isDisable = !isDebug
    val cancelForge = JFXButton("X")
    cancelForge.layoutX = 36.0
    cancelForge.layoutY = 53.0
    size.modifyWindwosSize(forgePane, forge, downloadForge, cancelForge)
    val fabricPane = Pane()
    fabricPane.layoutX = 220.0
    fabricPane.layoutY = 96.0
    fabricPane.setPrefSize(160.0, 87.0)
    fabric.layoutX = 43.0
    fabric.layoutY = 27.0
    val downloadFabric = JFXButton("->")
    downloadFabric.layoutX = 73.0
    downloadFabric.layoutY = 53.0
    downloadFabric.setPrefSize(47.0, 23.0)
    val cancelFabric = JFXButton("X")
    cancelFabric.layoutX = 43.0
    cancelFabric.layoutY = 53.0
    size.modifyWindwosSize(fabricPane, fabric, downloadFabric, cancelFabric)
    val fabricAPIPane = Pane()
    fabricAPIPane.layoutX = 380.0
    fabricAPIPane.layoutY = 96.0
    fabricAPIPane.setPrefSize(160.0, 87.0)
    fabricAPI.layoutX = 43.0
    fabricAPI.layoutY = 27.0
    val downloadFabricAPI = JFXButton("->")
    downloadFabricAPI.layoutX = 73.0
    downloadFabricAPI.layoutY = 53.0
    downloadFabricAPI.setPrefSize(47.0, 23.0)
    val cancelFabricAPI = JFXButton("X")
    cancelFabricAPI.layoutX = 43.0
    cancelFabricAPI.layoutY = 53.0
    size.modifyWindwosSize(fabricAPIPane, fabricAPI, downloadFabricAPI, cancelFabricAPI)
    val quiltPane = Pane()
    quiltPane.layoutX = 60.0
    quiltPane.layoutY = 183.0
    quiltPane.setPrefSize(160.0, 87.0)
    quilt.layoutX = 43.0
    quilt.layoutY = 27.0
    val downloadQuilt = JFXButton("->")
    downloadQuilt.layoutX = 73.0
    downloadQuilt.layoutY = 53.0
    downloadQuilt.setPrefSize(47.0, 23.0)
    val cancelQuilt = JFXButton("X")
    cancelQuilt.layoutX = 43.0
    cancelQuilt.layoutY = 53.0
    size.modifyWindwosSize(quiltPane, quilt, downloadQuilt, cancelQuilt)
    val confirm = JFXButton("安装游戏")
    confirm.layoutX = 435.0
    confirm.layoutY = 337.0
    AnchorPane.setRightAnchor(confirm, 30.0)
    AnchorPane.setBottomAnchor(confirm, 30.0)
    confirm.styleClass.add("BackGroundWriteButton")
    confirm.setPrefSize(107.0, 57.0)
    pane.background = Consoler.background
    size.modifyWindwosSize(pane, back, title, confirm, forgePane, fabricPane, fabricAPIPane, quiltPane)
    Consoler.setCss("ModChoosePane", fabricPane, forgePane, fabricAPIPane, quiltPane)
    Consoler.setCss("BlackBorder", back, downloadForge, downloadFabric, downloadFabricAPI, downloadQuilt)
    Consoler.setCss("BlackBorder", cancelForge, cancelFabric, cancelFabricAPI, cancelQuilt)
    pane.setStylesheets()
    MainStage.setScene(Scene(pane))
    downloadForge.onAction = EventHandler {
      try {
        val Choose = ModChooseVersionWindow(KindOfMod.FORGE, MainStage, launcher)
        Choose.setModChooser()
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
      }
    }
    cancelForge.onAction = EventHandler {
      launcher.cleanKind()
      downloadFabricAPI.isDisable = false
      downloadFabric.isDisable = false
      forge.text = Tips.ForgeNo
      fabric.text = Tips.FabricNo
    }
    downloadFabric.onAction = EventHandler {
      try {
        val Choose = ModChooseVersionWindow(KindOfMod.FABRIC, MainStage, launcher)
        Choose.setModChooser()
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
      }
    }
    cancelFabric.onAction = EventHandler {
      try {
        launcher.cleanKind()
        launcher.fabricModInstallInfo!!.apiDownloadTask = null
        downloadForge.isDisable = false
        downloadFabricAPI.isDisable = true
        forge.text = Tips.ForgeNo
        fabric.text = Tips.FabricNo
        fabricAPI.text = Tips.FabricAPINo
      } catch (e: NullPointerException) {
        logmaker.warn("warn:", e)
      }
    }
    downloadFabricAPI.onAction = EventHandler {
      try {
        val Choose = ModChooseVersionWindow(KindOfMod.FABRICAPI, MainStage, launcher)
        Choose.setModChooser()
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
      }
    }
    cancelFabricAPI.onAction = EventHandler {
      try {
        launcher.fabricModInstallInfo!!.apiDownloadTask = null
        fabricAPI.text = Tips.FabricAPINo
      } catch (e: NullPointerException) {
        logmaker.warn("warn:", e)
      }
    }
    downloadQuilt.onAction = EventHandler {
      try {
        val Choose = ModChooseVersionWindow(KindOfMod.QUILT, MainStage, launcher)
        Choose.setModChooser()
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
      }
    }
    cancelQuilt.onAction = EventHandler {
      quilt.text = Tips.QuiltNo
      forge.text = Tips.ForgeNo
      fabric.text = Tips.FabricNo
      downloadForge.isDisable = false
      downloadFabric.isDisable = false
    }
    if (gameModIsForge(launcher)) {
      forge.text = "Froge : " + launcher.forgeModDownloadInfo!!.modVersion
      fabric.text = "Fabric : 与Forge不兼容"
      downloadFabricAPI.isDisable = true
      downloadFabric.isDisable = true
    } else {
      forge.text = Tips.ForgeNo
    }
    if (gameModIsFabric(launcher)) {
      fabric.text = "Fabric : " + launcher.fabricModInstallInfo!!.modVersion
      forge.text = "Forge : 与Fabric不兼容"
      downloadFabricAPI.isDisable = false
      downloadForge.isDisable = true
      if (launcher.fabricModInstallInfo!!.isAPIDownloadTaskNoNull) {
        fabricAPI.text = launcher.fabricModInstallInfo!!.apiDownloadTask!!.fabricAPIVersionNumber
      }
    } else {
      fabric.text = Tips.FabricNo
      downloadFabricAPI.isDisable = true
    }
    if (gameModIsQuilt(launcher)) {
      quilt.text = "Quilt : " + launcher.quiltModDownloadInfo!!.modVersion
      forge.text = "Forge : 与Quilt不兼容"
      fabric.text = "Fabric :  与Quilt不兼容"
      downloadFabric.isDisable = true
      downloadForge.isDisable = true
    }
    back.onAction = EventHandler {
      GameVersionListWindow.setWindowScene(
        MainStage
      )
    }
    confirm.onAction = EventHandler {
      val gameDownloadingWindow = GameDownloadingWindow(
        launcher
      )
      gameDownloadingWindow.setDownGameWin(MainStage)
    }
  }

  private object Tips {
    var ForgeNo = "Forge : 不安装"
    var FabricNo = "Fabric : 不安装"
    var FabricAPINo = "FabricAPI : 不安装"
    var QuiltNo = "Quilt : 不安装"
  }

  companion object {
    private val logmaker = getLogger(ModChooseWindow::class.java)
  }
}
