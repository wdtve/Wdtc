package org.wdt.wdtc.ui.window.user

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import org.wdt.wdtc.core.impl.auth.accounts.OfflineAccounts
import org.wdt.wdtc.core.openapi.auth.UsersList.Companion.saveChangeToFile
import org.wdt.wdtc.core.openapi.auth.currentUsersList
import org.wdt.wdtc.core.openapi.utils.launchOnIO
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.ui.window.eventHandler
import org.wdt.wdtc.ui.window.tryCatching
import java.util.regex.Pattern

object OfflineUserWindow {
	fun setUserWin(pane: Pane) {
		pane.setPrefSize(600.0, 400.0)
		val registerUserName = TextField().apply {
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
			onAction = eventHandler {
				tryCatching {
					registerUserName.text.let {
						if (it.isQualified) {
							launchOnIO {
								currentUsersList.apply {
									OfflineAccounts(it).addToList()
								}.saveChangeToFile()
							}
							logmaker.info("离线账户${it}注册成功")
							UserListPane.setUserList(pane)
						} else {
							oKRegister.text = "不能带中文字符哦"
						}
					}
				}
			}
		}
		pane.children.run {
			clear()
			addAll(registerUserName, label, button, attention, oKRegister)
		}
		
	}
	
	private val String.isQualified: Boolean
		get() = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,999}$").matcher(this).find()
	
}
