package org.wdt.wdtc.ui.window

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger

object ExceptionWindow {
  private val logmaker = getLogger(ExceptionWindow::class.java)
  fun setErrorWin(e: Throwable) {
    logmaker.error("Error", e)
    setWin(e.getExceptionMessage(), "发生错误!")
  }

  fun setWin(e: String?, title: String?) {
    val runnable = Runnable {
      val stage = Stage()
      stage.setWidth(1000.0)
      stage.setHeight(600.0)
      val label = TextArea()
      label.text = e
      label.font = Font(14.0)
      label.prefHeight = stage.height
      label.prefWidth = stage.width
      val vBox = VBox()
      stage.title = title
      stage.icons.add(Image("assets/icon/ico.jpg"))
      val size = WindwosSizeManger(stage)
      size.modifyWindwosSize(vBox, label)
      stage.setScene(Scene(vBox))
      stage.show()
      stage.onCloseRequest = EventHandler { _: WindowEvent? -> vBox.children.clear() }
    }
    Platform.runLater(runnable)
  }
}
