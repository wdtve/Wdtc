package org.wdt.wdtc.ui.window.user

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.Offline
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.Yggdrasil
import org.wdt.wdtc.core.auth.getPreferredUser
import org.wdt.wdtc.core.auth.getUser
import org.wdt.wdtc.core.auth.setUserToJson
import org.wdt.wdtc.core.auth.userList
import org.wdt.wdtc.core.manger.userListFile
import org.wdt.wdtc.ui.window.setCss
import org.wdt.wdtc.ui.window.setErrorWin
import org.wdt.wdtc.ui.window.setStylesheets
import java.io.IOException

object UserListPane {
  private val preferredUser = getPreferredUser()
  fun setUserList(pane: Pane) {
    pane.children.clear()
    val scrollPane = ScrollPane()
    scrollPane.setPrefSize(600.0, 400.0)
    val vBox = VBox()
    vBox.setPrefSize(595.0, 395.0)
    if (userList.isNotEmpty()) {
      for (newUser in userList) {
        val userName = newUser.userName
        val userPane = AnchorPane()
        userPane.setPrefSize(595.0, 40.0)
        val enter = RadioButton()
        AnchorPane.setTopAnchor(enter, 15.0)
        AnchorPane.setBottomAnchor(enter, 15.0)
        AnchorPane.setLeftAnchor(enter, 15.0)
        enter.onAction = EventHandler {
          setUserToJson(getUser(userName))
          setUserList(pane)
        }
        if (newUser == preferredUser) {
          enter.isSelected = true
        }
        val image: Image? = try {
          Image(newUser.headFile.newInputStream())
        } catch (e: IOException) {
          setErrorWin(e)
          null
        }
        val head = ImageView(image ?: throw RuntimeException())
        head.fitHeight = 32.0
        head.fitWidth = 32.0
        AnchorPane.setTopAnchor(head, 15.0)
        AnchorPane.setBottomAnchor(head, 15.0)
        AnchorPane.setLeftAnchor(head, 50.0)
        val userNameLabel = Label(newUser.userName)
        AnchorPane.setTopAnchor(userNameLabel, 10.0)
        AnchorPane.setLeftAnchor(userNameLabel, 96.0)
        val userTypeLabel = Label()
        when (newUser.type) {
          Offline -> userTypeLabel.text = "离线账户"
          Yggdrasil -> userTypeLabel.text = "Yggdrasil外置登录"
          else -> TODO()
        }
        AnchorPane.setTopAnchor(userTypeLabel, 30.0)
        AnchorPane.setLeftAnchor(userTypeLabel, 96.0)
        val detele = JFXButton("删除")
        AnchorPane.setTopAnchor(detele, 17.0)
        AnchorPane.setLeftAnchor(detele, 530.0)
        detele.onAction = EventHandler {
          try {
            val userListObject = userListFile.readFileToJsonObject()
            userListObject.remove(userName)
            userListFile.writeObjectToFile(userListObject, Json.getBuilder().setPrettyPrinting())
            setUserList(pane)
          } catch (e: IOException) {
            setErrorWin(e)
          }
        }
        setCss("BlackBorder", userPane)
        userPane.children.addAll(enter, head, userNameLabel, userTypeLabel, detele)
        vBox.children.add(userPane)
      }
    }
    vBox.setStylesheets()
    scrollPane.content = vBox
    pane.children.add(scrollPane)
  }
}
