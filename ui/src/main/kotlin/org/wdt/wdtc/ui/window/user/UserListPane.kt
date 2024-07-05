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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.openapi.auth.*
import org.wdt.wdtc.core.openapi.auth.AccountsType.OFFLINE
import org.wdt.wdtc.core.openapi.auth.AccountsType.YGGDRASIL
import org.wdt.wdtc.ui.window.*
import java.io.IOException

object UserListPane {
	fun setUserList(pane: Pane) {
		val vBox = VBox().apply {
			setPrefSize(595.0, 395.0)
			setStylesheets()
		}
		launchOnJavaFx {
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
				val head = getHeadImage(newUser)
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
				}.also {
					setCss("BlackBorder", it)
					it.children.addAll(enter, head, userNameLabel, userTypeLabel, detele)
					vBox.children.add(it)
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
	
	private suspend fun getHeadImage(newUser: User): ImageView = coroutineScope {
		val image = async(Dispatchers.IO) { Image(newUser.headFile.newInputStream()) }
		ImageView(image.await()).apply {
			fitHeight = 32.0
			fitWidth = 32.0
			setTopAnchor(15.0)
			setBottomAnchor(15.0)
			setLeftAnchor(50.0)
		}
	}
}
