package org.wdt.wdtc.ui.window

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.wdt.wdtc.core.download.fabric.FabricAPIDownloadTask
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.fabric.FabricVersionList
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.forge.ForgeVersionList
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.download.quilt.QuiltVersionList
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.KindOfMod
import java.io.IOException

class ModChooseVersionWindow(
  private val kind: KindOfMod,
  private val mainStage: Stage,
  private val launcher: Launcher
) {
  private val size = mainStage.getSizeManger()

  @get:Throws(IOException::class)
  val modVersionList: VersionListInterface
    get() = when (kind) {
      KindOfMod.FORGE -> {
        ForgeVersionList(launcher)
      }

      KindOfMod.FABRICAPI -> {
        FabricAPIVersionList(launcher)
      }

      KindOfMod.QUILT -> {
        QuiltVersionList(launcher)
      }

      else -> {
        FabricVersionList()
      }
    }

  @Throws(IOException::class)
  fun setModChooser() {
    val back = JFXButton("返回")
    back.styleClass.add("BlackBorder")
    val pane = Pane()
    val buttonList = VBox()
    val list = ScrollPane()
    val tips = Label("选择一个Mod版本:")
    tips.layoutX = 149.0
    tips.layoutY = 67.0
    list.layoutY = 134.0
    list.setPrefSize(600.0, 316.0)
    Platform.runLater {
      try {
        for (versionJsonObject in modVersionList.versionList) {
          val versionButton = getVersionButton(versionJsonObject, buttonList)
          size.modifyWindwosSize(buttonList, versionButton)
        }
      } catch (e: IOException) {
        ExceptionWindow.setErrorWin(e)
      }
    }
    list.content = buttonList
    size.modifyWindwosSize(pane, list, back, tips)
    pane.background = background
    pane.setStylesheets()
    mainStage.setScene(Scene(pane))
    back.onAction = EventHandler {
      val choose = ModChooseWindow(launcher, mainStage)
      choose.setChooseWin()
    }
  }

  private fun getVersionButton(versionJsonObject: VersionJsonObjectInterface, buttonList: VBox): JFXButton {
    val versionButton = JFXButton(versionJsonObject.versionNumber)
    versionButton.style = "-fx-border-color: #000000"
    versionButton.prefWidth = 600.0
    versionButton.onAction = EventHandler {
      when (kind) {
        KindOfMod.FORGE -> launcher.forgeModDownloadInfo = ForgeDownloadInfo(launcher, versionJsonObject)
        KindOfMod.FABRIC -> launcher.fabricModInstallInfo = FabricDonwloadInfo(launcher, versionJsonObject)
        KindOfMod.QUILT -> launcher.quiltModDownloadInfo = QuiltInstallTask(launcher, versionJsonObject)
        KindOfMod.FABRICAPI -> launcher.fabricModInstallInfo?.apiDownloadTask =
          FabricAPIDownloadTask(launcher, versionJsonObject)

        else -> {}
      }
      val choose = ModChooseWindow(launcher, mainStage)
      choose.setChooseWin()
      buttonList.children.clear()
    }
    return versionButton
  }
}
