package org.wdt.wdtc.ui.window

import javafx.beans.value.ObservableValue
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.stage.Stage
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting

class WindwosSizeManger(private val MainStage: Stage) {
  fun modifyWindwosSize(pane: Pane, vararg node: Region) {
    pane.children.addAll(*node)
    for (region in node) {
      val widthratio = MainStage.width / windowsWidht
      region.layoutX *= widthratio
      if (region.prefWidth != -1.0) {
        region.prefWidth *= widthratio
      }
      MainStage.widthProperty()
        .addListener { _: ObservableValue<out Number>?, oldValue: Number, newValue: Number ->
          val ratio = newValue.toDouble() / oldValue.toDouble()
          region.layoutX *= ratio
          if (region.prefWidth != -1.0) {
            region.prefWidth *= ratio
          }
        }
      //            double heigthratio = MainStage.getHeight() / WindowsHeight;
//            region.setLayoutY(region.getLayoutY() * heigthratio);
//            if (region.getPrefHeight() != -1) {
//                region.setPrefHeight(region.getPrefHeight() * heigthratio);
//            }

//            MainStage.heightProperty().addListener((observable, oldValue, newValue) -> {
//                double ratio = newValue.doubleValue() / oldValue.doubleValue();
//                region.setLayoutY(region.getLayoutY() * ratio);
//                if (region.getPrefHeight() != -1) {
//                    region.setPrefHeight(region.getPrefHeight() * ratio);
//                }
//            });
    }
  }

  fun setWindwosSize() {
    val (_, _, _, _, _, _, windowsWidth, windowsHeight) = setting
    MainStage.width = windowsWidth
    MainStage.height = windowsHeight
  }

  override fun toString(): String {
    return "WindwosSize{" + "MainStageSize=" + MainStage.width + "," + MainStage.height + "}"
  }

  companion object {
    const val windowsWidht = 616.0
    const val windowsHeight = 489.0
    fun Stage.getSizeManger(): WindwosSizeManger {
      return WindwosSizeManger(this)
    }
  }
}
