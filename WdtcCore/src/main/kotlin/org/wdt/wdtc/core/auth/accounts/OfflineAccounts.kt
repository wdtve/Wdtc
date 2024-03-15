package org.wdt.wdtc.core.auth.accounts

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.auth.LoginUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.utils.*
import java.util.*

class OfflineAccounts(private val username: String) : LoginUser {
	
	private val headFile = ioCoroutineScope.async {
		SkinUtils(SkinUtils.getUserSkinFile(username)).apply {
			userSkinInput = getResourceAsStream("/assets/skin/steve.png")
		}.writeSkinHead()
	}
	private val uuid = defaultCoroutineScope.async {
		UUID.randomUUID().toString().cleanStrInString("-")
	}
	
	override val user: User
		get() = runBlocking {
			User(
				username,
				"\${auth_access_token}",
				AccountsType.OFFLINE,
				uuid.await(),
				headFile.await()
			)
		}
}
