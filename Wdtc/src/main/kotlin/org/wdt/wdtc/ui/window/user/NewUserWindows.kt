package org.wdt.wdtc.ui.window.user

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.littleskinUrl
import org.wdt.wdtc.core.utils.openSomething
import org.wdt.wdtc.ui.window.*

class NewUserWindows(private val mainStage: Stage) {
  var type: AccountsType? = null
  var title: String? = null

  fun show() {
    val userStage = Stage().apply {
      width = 725.0
      height = 430.0
      icons.add(Image("/assets/icon/ico.jpg"))
      initOwner(mainStage)
      title = this@NewUserWindows.title
      initModality(Modality.APPLICATION_MODAL)
      isResizable = false
    }
    val sonPane = Pane().apply {
      setTopLowerRight()
      setBottomAnchor(0.0)
      setLeftAnchor(125.0)
      setPrefSize(600.0, 400.0)
    }
    sonPane.let {
      if (type == null) {
        UserListPane.setUserList(it)
      } else {
        when (type) {
          AccountsType.YGGDRASIL -> LittleskinWindow.setLittleskinWin(it)
          AccountsType.OFFLINE -> OfflineUserWindow.setUserWin(it)
          else -> TODO()
        }
      }
    }
    val userListButton = JFXButton("账户列表").apply {
      onAction = EventHandler { UserListPane.setUserList(sonPane) }
      setPrefSize(125.0, 30.0)
    }
    val offlineButton = JFXButton("离线账户").apply {
      setTopAnchor(30.0)
      setPrefSize(125.0, 30.0)
      onAction = EventHandler { OfflineUserWindow.setUserWin(sonPane) }
    }
    val littleskinCom = JFXButton("Littleskin官网").apply {
      setPrefSize(125.0, 30.0)
      setBottomAnchor(30.0)
      onAction = EventHandler { openSomething(littleskinUrl) }
    }
    val buygame = JFXButton("购买正版").apply {
      setPrefSize(125.0, 30.0)
      setBottomAnchor(0.0)
      onAction = EventHandler { openSomething("https://www.minecraft.net/zh-hans") }
    }
    val yggdrasilButton = JFXButton("Yggdrasil账户").apply {
      setPrefSize(125.0, 30.0)
      onAction = EventHandler { LittleskinWindow.setLittleskinWin(sonPane) }
      setTopAnchor(60.0)
    }
    val buttonLine = AnchorPane().apply {
      prefWidth = 125.0
      children.addAll(userListButton, offlineButton, yggdrasilButton, littleskinCom, buygame)
      styleClass.add("BlackBorder")
      setTopLowerLeft()
      setTopAnchor(0.0)
    }
    AnchorPane().apply {
      background = wdtcBackground
      children.addAll(buttonLine, sonPane)
      setStylesheets()
    }.let {
      userStage.run {
        scene = Scene(it)
        show()
      }
    }
  }
}
