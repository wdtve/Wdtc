package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.*
import org.wdt.wdtc.core.download.fabric.FabricAPIDownloadTask
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.fabric.FabricVersionList
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.forge.ForgeVersionList
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.download.quilt.QuiltVersionList
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.ckeckIsNull
import java.io.IOException

class ModChooseVersionWindow(
  private val kind: KindOfMod,
  private val mainStage: Stage,
  private val version: Version
) {
  private val size = mainStage.getSizeManger()

  private val modVersionList
    get() = when (kind) {
      KindOfMod.FORGE -> {
        ForgeVersionList(version).versionList
      }

      KindOfMod.FABRICAPI -> {
        FabricAPIVersionList(version).versionList
      }

      KindOfMod.QUILT -> {
        QuiltVersionList(version).versionList
      }

      else -> {
        FabricVersionList().versionList
      }
    }


  @Throws(IOException::class)
  fun setModChooser() {
    val back = JFXButton("返回").apply {
      styleClass.add("BlackBorder")
      onAction = EventHandler {
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
    try {
      CoroutineScope(Dispatchers.Unconfined).launch {
        modVersionList.forEach {
          size.modifyWindwosSize(buttonList, getVersionButton(it, buttonList))
        }
      }
    } catch (e: IOException) {
      setErrorWin(e)
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
      size.modifyWindwosSize(it, list, back, tips)
      mainStage.setScene(Scene(it))
    }
  }

  private fun getVersionButton(versionJsonObject: VersionsJsonObjectInterface, buttonList: VBox): JFXButton {
    return JFXButton(versionJsonObject.versionNumber).apply {
      style = "-fx-border-color: #000000"
      prefWidth = 580.0
      onAction = EventHandler {
        when (kind) {
          KindOfMod.FORGE -> version.forgeModDownloadInfo = ForgeDownloadInfo(version, versionJsonObject)
          KindOfMod.FABRIC -> version.fabricModInstallInfo = FabricDonwloadInfo(version, versionJsonObject)
          KindOfMod.QUILT -> version.quiltModDownloadInfo = QuiltInstallTask(version, versionJsonObject)
          KindOfMod.FABRICAPI -> version.fabricModInstallInfo.ckeckIsNull().apiDownloadTask =
            FabricAPIDownloadTask(version, versionJsonObject)

          else -> {}
        }
        ModChooseWindow(version, mainStage).run {
          setChooseWin()
        }
        buttonList.children.clear()
      }
    }
  }
}
