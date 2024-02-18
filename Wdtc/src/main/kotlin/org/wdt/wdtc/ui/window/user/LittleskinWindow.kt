package org.wdt.wdtc.ui.window.user

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import kotlinx.coroutines.launch
import org.wdt.wdtc.core.auth.UsersList.Companion.saveChangeToFile
import org.wdt.wdtc.core.auth.currentUsersList
import org.wdt.wdtc.core.auth.yggdrasil.YggdrasilAccounts
import org.wdt.wdtc.core.manger.littleskinUrl
import org.wdt.wdtc.core.utils.ioCoroutineScope
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.toCoroutineName
import org.wdt.wdtc.core.utils.warning
import java.io.IOException

object LittleskinWindow {
  fun setLittleskinWin(pane: Pane) {
    val littleskinTitle = Label("Littleskin外置登录").apply {
      layoutX = 250.0
      layoutY = 69.0
    }
    val username = Label("用户名").apply {
      layoutX = 175.0
      layoutY = 107.0
    }
    val inputusername = TextField().apply {
      layoutX = 220.0
      layoutY = 103.0
    }
    val powerWordTip = Label("密码:").apply {
      layoutX = 179.0
      layoutY = 135.0
    }
    val inputpowerword = TextField().apply {
      layoutX = 221.0
      layoutY = 131.0
    }
    val label = Label().apply {
      layoutX = 220.0
      layoutY = 185.0
      prefHeight = 15.0
      prefWidth = 110.0
    }
    val ok = Button("登录").apply {
      layoutX = 267.0
      layoutY = 219.0
      onAction = EventHandler {
        val userName = inputusername.text
        val powerWord = inputpowerword.text
        if (userName.isEmpty() && powerWord.isEmpty()) {
          label.text = "请输入用户名、密码"
        } else {
          try {
            loginUser(userName, powerWord)
            UserListPane.setUserList(pane)
          } catch (e: IOException) {
            label.text = "用户名或密码错误"
            logmaker.warning("用户名或密码错误", e)
          }
        }
      }
    }
    pane.children.run {
      clear()
      addAll(littleskinTitle, username, inputusername, powerWordTip, inputpowerword, label, ok)
    }
  }

  @Throws(IOException::class)
  private fun loginUser(userName: String, powerWord: String) {
    YggdrasilAccounts(
      littleskinUrl,
      userName, powerWord
    ).run {
      currentUsersList.apply {
        this@run.addToList()
      }.saveChangeToFile()

      ioCoroutineScope.launch("Login ${user.userName}".toCoroutineName()) {
        yggdrasilTextures.run {
          startDownloadUserSkin()
        }
      }
      logmaker.info("Login $userName Littleskin User")
    }
  }
}
