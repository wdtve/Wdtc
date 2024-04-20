package org.wdt.wdtc.ui.window.user

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import org.wdt.wdtc.core.impl.auth.accounts.OfflineAccounts
import org.wdt.wdtc.core.openapi.auth.UsersList.Companion.saveChangeToFile
import org.wdt.wdtc.core.openapi.auth.currentUsersList
import org.wdt.wdtc.core.openapi.utils.launchScope
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.runOnIO
import org.wdt.wdtc.ui.window.runOnJavaFx
import org.wdt.wdtc.ui.window.setErrorWin
import java.io.IOException
import java.util.regex.Pattern

object OfflineUserWindow {
	fun setUserWin(pane: Pane) {
		pane.setPrefSize(600.0, 400.0)
		val registerusername = TextField().apply {
			layoutX = 224.0
			layoutY = 106.0
		}
		val label = Label("用户名:").apply {
			layoutY = 110.0
			layoutX = 177.0
		}
		val attention =
			Label("用户名用于游戏内成就等地方,用户名不能包含空格、中文、波浪线等特殊字符，下划线、英文、数字允许").apply {
				layoutX = 22.0
				layoutY = 78.0
			}
		val oKRegister = Label().apply {
			layoutX = 284.0
			layoutY = 282.0
		}
		val button = Button("注册一个!").apply {
			layoutX = 266.0
			layoutY = 221.0
			onAction = EventHandler {
				try {
					registerusername.text.let {
						if (it.isQualified) {
							launchScope {
								runOnIO {
									currentUsersList.apply {
										OfflineAccounts(it).addToList()
									}.saveChangeToFile()
								}
								runOnJavaFx {
									logmaker.info("离线账户${it}注册成功")
									UserListPane.setUserList(pane)
								}
							}
						} else {
							oKRegister.text = "不能带中文字符哦"
						}
					}
				} catch (exception: IOException) {
					setErrorWin(exception)
				}
			}
		}
		pane.children.run {
			clear()
			addAll(registerusername, label, button, attention, oKRegister)
		}
		
	}
	
	private val String.isQualified: Boolean
		get() = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,999}$").matcher(this).find()
	
}
