package org.wdt.wdtc.ui.window.user

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.URLManger.littleskinUrl
import org.wdt.wdtc.core.utils.URLUtils.openSomething
import org.wdt.wdtc.ui.window.Consoler
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.Consoler.setTopLowerLeft
import org.wdt.wdtc.ui.window.Consoler.setTopLowerRight

class NewUserWindows(private val mainStage: Stage) {
  var type: AccountsType? = null
  var title: String? = null

  fun show() {
    val userStage = Stage()
    userStage.setWidth(725.0)
    userStage.setHeight(430.0)
    val parentPane = AnchorPane()
    userStage.icons.add(Image("/assets/icon/ico.jpg"))
    val sonPane = Pane()
    if (type == null) {
      UserListPane.setUserList(sonPane)
    } else {
      when (type) {
        AccountsType.Yggdrasil -> LittleskinWindow.setLittleskinWin(sonPane)
        AccountsType.Offline -> OfflineUserWindow.setUserWin(sonPane)
        else -> TODO()
      }
    }
    userStage.initOwner(mainStage)
    userStage.title = title
    sonPane.setPrefSize(600.0, 400.0)
    val buttonLine = AnchorPane()
    buttonLine.prefWidth = 125.0
    val UserListButton = JFXButton("账户列表")
    UserListButton.onAction = EventHandler { event: ActionEvent? -> UserListPane.setUserList(sonPane) }
    UserListButton.setPrefSize(125.0, 30.0)
    val OfflineButton = JFXButton("离线账户")
    AnchorPane.setTopAnchor(OfflineButton, 30.0)
    OfflineButton.setPrefSize(125.0, 30.0)
    OfflineButton.onAction = EventHandler { event: ActionEvent? -> OfflineUserWindow.setUserWin(sonPane) }
    val LittleskinCom = JFXButton("Littleskin官网")
    LittleskinCom.setPrefSize(125.0, 30.0)
    AnchorPane.setBottomAnchor(LittleskinCom, 30.0)
    val buygame = JFXButton("购买正版")
    buygame.setPrefSize(125.0, 30.0)
    AnchorPane.setBottomAnchor(buygame, 0.0)
    val YggdrasilButton = JFXButton("Yggdrasil账户")
    YggdrasilButton.setPrefSize(125.0, 30.0)
    YggdrasilButton.onAction = EventHandler { LittleskinWindow.setLittleskinWin(sonPane) }
    AnchorPane.setTopAnchor(YggdrasilButton, 60.0)
    sonPane.setTopLowerRight()
    AnchorPane.setBottomAnchor(sonPane, 0.0)
    AnchorPane.setLeftAnchor(sonPane, 125.0)
    buttonLine.children.addAll(UserListButton, OfflineButton, YggdrasilButton, LittleskinCom, buygame)
    buttonLine.styleClass.add("BlackBorder")
    buttonLine.setTopLowerLeft()
    AnchorPane.setTopAnchor(buttonLine, 0.0)
    parentPane.background = Consoler.background
    parentPane.children.addAll(buttonLine, sonPane)
    userStage.setScene(Scene(parentPane))
    parentPane.setStylesheets()
    userStage.initModality(Modality.APPLICATION_MODAL)
    userStage.isResizable = false
    userStage.show()
    LittleskinCom.onAction = EventHandler { openSomething(littleskinUrl) }
    buygame.onAction = EventHandler { openSomething("https://www.minecraft.net/zh-hans") }
  }
}
