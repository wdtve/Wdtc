package org.wdt.wdtc.ui.window.user

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import org.wdt.wdtc.core.auth.addUser
import org.wdt.wdtc.core.auth.yggdrasil.YggdrasilAccounts
import org.wdt.wdtc.core.manger.littleskinUrl
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.ui.window.setErrorWin
import java.io.IOException

object LittleskinWindow {
  fun setLittleskinWin(pane: Pane) {
    pane.children.clear()
    val littleskinTitle = Label("Littleskin外置登录")
    littleskinTitle.layoutX = 250.0
    littleskinTitle.layoutY = 69.0
    val username = Label("用户名")
    username.layoutX = 175.0
    username.layoutY = 107.0
    val Inputusername = TextField()
    Inputusername.layoutX = 220.0
    Inputusername.layoutY = 103.0
    val powerWordTip = Label("密码:")
    powerWordTip.layoutX = 179.0
    powerWordTip.layoutY = 135.0
    val inputpowerword = TextField()
    inputpowerword.layoutX = 221.0
    inputpowerword.layoutY = 131.0
    val label = Label()
    label.layoutX = 220.0
    label.layoutY = 185.0
    label.prefHeight = 15.0
    label.prefWidth = 110.0
    val ok = Button("登录")
    ok.layoutX = 267.0
    ok.layoutY = 219.0
    pane.children.addAll(littleskinTitle, username, Inputusername, powerWordTip, inputpowerword, label, ok)
    ok.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
      val UserName = Inputusername.text
      val PowerWord = inputpowerword.text
      if (UserName.isEmpty() && PowerWord.isEmpty()) {
        label.text = "请输入用户名、密码"
        logmaker.warn("用户名、密码为空")
      } else {
        try {
          val yggdrasilAccounts = YggdrasilAccounts(
            littleskinUrl,
            UserName, PowerWord
          )
          addUser(yggdrasilAccounts.user)
          try {
            val yggdrasilTextures = yggdrasilAccounts.yggdrasilTextures
            yggdrasilTextures.startDownloadUserSkin()
            logmaker.info("Littleskin用户:" + UserName + "登陆成功!")
            UserListPane.setUserList(pane)
          } catch (e: IOException) {
            setErrorWin(e)
          }
        } catch (e: IOException) {
          label.text = "用户名或密码错误"
          logmaker.warn("用户名或密码错误", e)
        }
      }
    }
  }
}
