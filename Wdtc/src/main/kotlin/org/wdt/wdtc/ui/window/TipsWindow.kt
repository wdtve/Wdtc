package org.wdt.wdtc.ui.window

import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import javafx.stage.Stage

class TipsWindow(private val tips: String, private val MainStage: Stage) {
  private val title: String? = null
  fun show() {
    val TipStage = Stage()
    TipStage.initOwner(MainStage)
    TipStage.initModality(Modality.WINDOW_MODAL)
    val pane = AnchorPane()
    val label = Label(tips)
    AnchorPane.setLeftAnchor(label, 10.0)
    AnchorPane.setRightAnchor(label, 10.0)
    TipStage.show()
  }
}
