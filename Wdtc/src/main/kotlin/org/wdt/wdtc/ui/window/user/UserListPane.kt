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
import kotlinx.coroutines.launch
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.OFFLINE
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType.YGGDRASIL
import org.wdt.wdtc.core.auth.changeListToFile
import org.wdt.wdtc.core.auth.currentUsersList
import org.wdt.wdtc.core.auth.preferredUser
import org.wdt.wdtc.core.auth.setUserToJson
import org.wdt.wdtc.core.utils.noNull
import org.wdt.wdtc.ui.window.*
import java.io.IOException

object UserListPane {
	fun setUserList(pane: Pane) {
		val preferredUser = preferredUser
		val vBox = VBox().apply {
			setPrefSize(595.0, 395.0)
			setStylesheets()
		}
		javafxCoroutineScope.launch {
			currentUsersList.forEach { newUser ->
				val enter = RadioButton().apply {
					setTopAnchor(15.0)
					setBottomAnchor(15.0)
					setLeftAnchor(15.0)
					onAction = EventHandler {
						setUserToJson(newUser)
						isSelected = true
						setUserList(pane)
					}
					if (preferredUser == newUser) {
						isSelected = true
					}
				}
				val head = try {
					Image(newUser.headFile.newInputStream())
				} catch (e: IOException) {
					setErrorWin(e)
					null
				}.let {
					ImageView(it.noNull())
				}.apply {
					fitHeight = 32.0
					fitWidth = 32.0
					setTopAnchor(15.0)
					setBottomAnchor(15.0)
					setLeftAnchor(50.0)
				}
				val userNameLabel = Label(newUser.userName).apply {
					setTopAnchor(10.0)
					setLeftAnchor(96.0)
				}
				val userTypeLabel = Label().apply {
					text = when (newUser.type) {
						OFFLINE -> "离线账户"
						YGGDRASIL -> "Yggdrasil外置登录"
						else -> TODO()
					}
					setTopAnchor(30.0)
					setLeftAnchor(96.0)
				}
				val detele = JFXButton("删除").apply {
					setTopAnchor(17.0)
					setLeftAnchor(530.0)
					onAction = EventHandler {
						try {
							currentUsersList.changeListToFile {
								remove(newUser)
							}
							setUserList(pane)
						} catch (e: IOException) {
							setErrorWin(e)
						}
					}
				}
				AnchorPane().apply {
					setPrefSize(595.0, 40.0)
				}.run {
					setCss("BlackBorder", this)
					children.addAll(enter, head, userNameLabel, userTypeLabel, detele)
					vBox.children.add(this)
				}
			}
			
		}
		ScrollPane().apply {
			setPrefSize(600.0, 400.0)
			content = vBox
		}.let {
			pane.children.run {
				clear()
				add(it)
			}
		}
	}
}
