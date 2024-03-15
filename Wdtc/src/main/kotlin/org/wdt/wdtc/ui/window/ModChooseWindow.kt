package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.isDebug
import org.wdt.wdtc.core.utils.*
import java.io.IOException

class ModChooseWindow(private val version: Version, private val mainStage: Stage) {
  private val size: WindwosSizeManger = mainStage.getSizeManger()
  fun setChooseWin() {
    val pane = Pane()
    val back = JFXButton("返回")
    val title = Label(version.versionNumber)
    title.layoutX = 283.0
    title.layoutY = 69.0
    val forge = Label(Tips.FORGE_NO)
    val fabric = Label(Tips.FABRIC_NO)
    val fabricAPI = Label(Tips.FABRIC_API_NO)
    val quilt = Label(Tips.QUILT_NO)
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
    pane.background = wdtcBackground
    size.modifyWindwosSize(pane, back, title, confirm, forgePane, fabricPane, fabricAPIPane, quiltPane)
    setCss("ModChoosePane", fabricPane, forgePane, fabricAPIPane, quiltPane)
    setCss("BlackBorder", back, downloadForge, downloadFabric, downloadFabricAPI, downloadQuilt)
    setCss("BlackBorder", cancelForge, cancelFabric, cancelFabricAPI, cancelQuilt)
    pane.setStylesheets()
    mainStage.setScene(Scene(pane))
    downloadForge.onAction = EventHandler {
      try {
        val choose = ModChooseVersionWindow(KindOfMod.FORGE, mainStage, version)
        choose.setModChooser()
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
    cancelForge.onAction = EventHandler {
      version.cleanKind()
      downloadFabricAPI.isDisable = false
      downloadFabric.isDisable = false
      forge.text = Tips.FORGE_NO
      fabric.text = Tips.FABRIC_NO
    }
    downloadFabric.onAction = EventHandler {
      try {
        val choose = ModChooseVersionWindow(KindOfMod.FABRIC, mainStage, version)
        choose.setModChooser()
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
    cancelFabric.onAction = EventHandler {
      try {
        version.cleanKind()
        version.fabricModInstallInfo?.apiDownloadTask = null
        downloadForge.isDisable = false
        downloadFabricAPI.isDisable = true
        forge.text = Tips.FORGE_NO
        fabric.text = Tips.FABRIC_NO
        fabricAPI.text = Tips.FABRIC_API_NO
      } catch (e: NullPointerException) {
        logmaker.warning("warn:", e)
      }
    }
    downloadFabricAPI.onAction = EventHandler {
      try {
        val choose = ModChooseVersionWindow(KindOfMod.FABRICAPI, mainStage, version)
        choose.setModChooser()
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
    cancelFabricAPI.onAction = EventHandler {
      try {
        version.fabricModInstallInfo?.apiDownloadTask = null
        fabricAPI.text = Tips.FABRIC_API_NO
      } catch (e: NullPointerException) {
        logmaker.warning(e.getExceptionMessage())
      }
    }
    downloadQuilt.onAction = EventHandler {
      try {
        val choose = ModChooseVersionWindow(KindOfMod.QUILT, mainStage, version)
        choose.setModChooser()
      } catch (e: IOException) {
        setErrorWin(e)
      }
    }
    cancelQuilt.onAction = EventHandler {
      quilt.text = Tips.QUILT_NO
      forge.text = Tips.FORGE_NO
      fabric.text = Tips.FABRIC_NO
      downloadForge.isDisable = false
      downloadFabric.isDisable = false
    }
    if (version.isForge) {
      forge.text = "Froge : " + version.forgeModDownloadInfo?.modVersion
      fabric.text = "Fabric : 与Forge不兼容"
      downloadFabricAPI.isDisable = true
      downloadFabric.isDisable = true
    } else {
      forge.text = Tips.FORGE_NO
    }
    if (version.isFabric) {
      fabric.text = "Fabric : " + version.fabricModInstallInfo?.modVersion
      forge.text = "Forge : 与Fabric不兼容"
      downloadFabricAPI.isDisable = false
      downloadForge.isDisable = true
      val fabircModInstallInfo = version.fabricModInstallInfo
      if (fabircModInstallInfo?.isAPIDownloadTaskNoNull == true) {
        fabricAPI.text = fabircModInstallInfo.apiDownloadTask?.fabricAPIVersionNumber
      }
    } else {
      fabric.text = Tips.FABRIC_NO
      downloadFabricAPI.isDisable = true
    }
    if (version.isQuilt) {
      quilt.text = "Quilt : " + version.quiltModDownloadInfo?.modVersion
      forge.text = "Forge : 与Quilt不兼容"
      fabric.text = "Fabric :  与Quilt不兼容"
      downloadFabric.isDisable = true
      downloadForge.isDisable = true
    }
    back.onAction = EventHandler {
	    GameVersionListWindow().setWindowScene(mainStage)
    }
    confirm.onAction = EventHandler {
      GameDownloadingWindow(version).run {
        setDownGameWin(mainStage)
      }
    }
  }

  private object Tips {
    const val FORGE_NO = "Forge : 不安装"
    const val FABRIC_NO = "Fabric : 不安装"
    const val FABRIC_API_NO = "FabricAPI : 不安装"
    const val QUILT_NO = "Quilt : 不安装"
  }

}
