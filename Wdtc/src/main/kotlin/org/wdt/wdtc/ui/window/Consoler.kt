package org.wdt.wdtc.ui.window

import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.layout.*
import org.wdt.wdtc.core.manger.VMManger.isDebug
import org.wdt.wdtc.core.manger.VMManger.launcherVersion
import java.util.*

object Consoler {
  val background: Background
    get() {
      //Form:https://www.bilibili.com/video/BV1EY411m7uZ
      val image = Image(
        Objects.requireNonNull(
          Consoler::class.java.getResourceAsStream("/assets/blackGround/BlackGround.jpg")
        )
      )
      return Background(
        BackgroundImage(
          image,
          BackgroundRepeat.NO_REPEAT,
          BackgroundRepeat.NO_REPEAT,
          BackgroundPosition.CENTER,
          BackgroundSize(
            WindwosSizeManger.Companion.windowsWidht,
            WindwosSizeManger.Companion.windowsHeight,
            false,
            false,
            true,
            true
          )
        )
      )
    }
  val cssFile: String
    get() = Consoler::class.java.getResource("/css/color.css").toString()

  fun setCss(id: String?, vararg pane: Region) {
    for (region in pane) {
      region.styleClass.add(id)
    }
  }

  fun Pane.setStylesheets() {
    this.stylesheets.add(cssFile)
  }

  fun Node.setTopGrid() {
    setTopLowerLeft()
    AnchorPane.setTopAnchor(this, 0.0)
    AnchorPane.setLeftAnchor(this, 0.0)
  }

  fun Node.setTopLowerLeft() {
    AnchorPane.setBottomAnchor(this, 0.0)
    AnchorPane.setLeftAnchor(this, 0.0)
  }

  fun Node.setTopLowerRight() {
    AnchorPane.setBottomAnchor(this, 0.0)
    AnchorPane.setRightAnchor(this, 0.0)
  }

  fun getWindowsTitle(windowsName: String?): String {
    return String.format(if (isDebug) "Wdtc - Debug - %s - %s" else "Wdtc - %s - %s", launcherVersion, windowsName)
  }

  val windowsTitle: String
    get() = String.format(if (isDebug) "Wdtc - Debug - %s" else "Wdtc - %s", launcherVersion)
}
