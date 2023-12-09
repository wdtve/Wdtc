package org.wdt.wdtc.ui.window.user

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import org.wdt.wdtc.core.auth.accounts.OfflineAccounts
import org.wdt.wdtc.core.auth.addUser
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.ui.window.ExceptionWindow
import java.io.IOException
import java.util.regex.Pattern

object OfflineUserWindow {
  fun setUserWin(pane: Pane) {
    pane.children.clear()
    pane.setPrefSize(600.0, 400.0)
    val Registerusername = TextField()
    Registerusername.layoutX = 224.0
    Registerusername.layoutY = 106.0
    val label = Label("用户名:")
    label.layoutY = 110.0
    label.layoutX = 177.0
    val button = Button("注册一个!")
    button.layoutX = 266.0
    button.layoutY = 221.0
    val attention = Label("用户名用于游戏内成就等地方,用户名不能包含空格、中文、波浪线等特殊字符，下划线、英文、数字允许")
    attention.layoutX = 22.0
    attention.layoutY = 78.0
    val OKRegister = Label()
    OKRegister.layoutX = 284.0
    OKRegister.layoutY = 282.0
    pane.children.addAll(Registerusername, label, button, attention, OKRegister)
    button.onAction = EventHandler { event: ActionEvent? ->
      try {
        val username = Registerusername.text
        if (isQualified(username)) {
          val offline = OfflineAccounts(username)
          addUser(offline.user)
          logmaker.info("离线账户" + username + "注册成功")
          UserListPane.setUserList(pane)
        } else {
          OKRegister.text = "不能带中文字符哦"
        }
      } catch (exception: IOException) {
        ExceptionWindow.setErrorWin(exception)
      }
    }
  }

  fun isQualified(str: String?): Boolean {
    return Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,999}$").matcher(str).find()
  }
}
