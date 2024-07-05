package org.wdt.wdtc.ui.window.user

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import kotlinx.coroutines.coroutineScope
import org.wdt.wdtc.core.impl.auth.accounts.yggdrasil.YggdrasilAccounts
import org.wdt.wdtc.core.openapi.auth.changeListToFile
import org.wdt.wdtc.core.openapi.auth.currentUsersList
import org.wdt.wdtc.core.openapi.manager.littleskinUrl
import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.runOnIO
import org.wdt.wdtc.core.openapi.utils.warning
import org.wdt.wdtc.ui.window.eventHandler
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
			onAction = eventHandler {
				val pair = inputusername.text to inputpowerword.text
				if (pair.toList().any { it.isEmpty() }) {
					label.text = "请输入用户名、密码"
				} else {
					try {
						runOnIO {
							loginUser(pair)
						}
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
	
	private suspend fun loginUser(pair: Pair<String, String>) = coroutineScope {
		val name = pair.first
		YggdrasilAccounts(littleskinUrl, pair.first, pair.second).run {
			
			launch("Login $name") {
				textures.startDownloadUserSkin()
			}
			
			runOnIO {
				currentUsersList.changeListToFile {
					this@run.addToList()
				}
			}
		}
		logmaker.info("Login $name Littleskin User")
	}
}
