package org.wdt.wdtc.ui.window.user

import com.jfoenix.controls.JFXButton
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.wdt.utils.gson.JsonUtils
import org.wdt.utils.io.FileUtils
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.User.Companion.setUserToJson
import org.wdt.wdtc.core.auth.UsersList.getUser
import org.wdt.wdtc.core.auth.UsersList.userList
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.Offline
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.Yggdrasil
import org.wdt.wdtc.core.manger.FileManger.userJson
import org.wdt.wdtc.core.manger.FileManger.userListFile
import org.wdt.wdtc.ui.window.Consoler
import org.wdt.wdtc.ui.window.Consoler.setStylesheets
import org.wdt.wdtc.ui.window.ExceptionWindow
import java.io.IOException

object UserListPane {
  private val UserFile = userJson
  private val UserListFile = userListFile
  fun setUserList(pane: Pane) {
    pane.children.clear()
    val scrollPane = ScrollPane()
    scrollPane.setPrefSize(600.0, 400.0)
    val vBox = VBox()
    vBox.setPrefSize(595.0, 395.0)
    val users = userList
    if (users.isNotEmpty()) {
      for (user in users) {
        val userName = user.userName
        val userPane = AnchorPane()
        userPane.setPrefSize(595.0, 40.0)
        val enter = RadioButton()
        AnchorPane.setTopAnchor(enter, 15.0)
        AnchorPane.setBottomAnchor(enter, 15.0)
        AnchorPane.setLeftAnchor(enter, 15.0)
        enter.onAction = EventHandler<ActionEvent> { event: ActionEvent? ->
          setUserToJson(getUser(userName!!))
          setUserList(pane)
        }
        if (user.equals(User.user)) {
          enter.isSelected = true
        }
        var image: Image? = null
        try {
          image = Image(FileUtils.newInputStream(user.headFile))
        } catch (e: IOException) {
          ExceptionWindow.setErrorWin(e)
        }
        val head = ImageView(image)
        head.fitHeight = 32.0
        head.fitWidth = 32.0
        AnchorPane.setTopAnchor(head, 15.0)
        AnchorPane.setBottomAnchor(head, 15.0)
        AnchorPane.setLeftAnchor(head, 50.0)
        val userNameLabel = Label(user.userName)
        AnchorPane.setTopAnchor(userNameLabel, 10.0)
        AnchorPane.setLeftAnchor(userNameLabel, 96.0)
        val userTypeLabel = Label()
        when (user.type) {
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
            val userListObject = JsonUtils.getJsonObject(userListFile)
            userListObject.remove(userName)
            JsonUtils.writeObjectToFile(UserListFile, userListObject)
            setUserList(pane)
          } catch (e: IOException) {
            ExceptionWindow.setErrorWin(e)
          }
        }
        Consoler.setCss("BlackBorder", userPane)
        userPane.children.addAll(enter, head, userNameLabel, userTypeLabel, detele)
        vBox.children.add(userPane)
      }
    }
    vBox.setStylesheets()
    scrollPane.content = vBox
    pane.children.add(scrollPane)
  }
}
