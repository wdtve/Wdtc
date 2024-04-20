package org.wdt.wdtc.core.impl.auth.accounts

import kotlinx.coroutines.coroutineScope
import org.wdt.wdtc.core.openapi.auth.Accounts.AccountsType
import org.wdt.wdtc.core.openapi.auth.LoginUser
import org.wdt.wdtc.core.openapi.auth.User
import org.wdt.wdtc.core.openapi.utils.SkinUtils
import org.wdt.wdtc.core.openapi.utils.cleanStrInString
import org.wdt.wdtc.core.openapi.utils.getResourceAsStream
import org.wdt.wdtc.core.openapi.utils.ioAsync
import java.util.*

class OfflineAccounts(private val username: String) : LoginUser {
	
	private val headFile = ioAsync {
		SkinUtils(SkinUtils.getUserSkinFile(username)).apply {
			userSkinInput = getResourceAsStream("/assets/skin/steve.png")
		}.writeSkinHead()
	}
	private val uuid = ioAsync {
		UUID.randomUUID().toString().cleanStrInString("-")
	}
	
	override suspend fun getUser(): User = coroutineScope {
		User(
			username, "\${auth_access_token}", AccountsType.OFFLINE, uuid.await(), headFile.await()
		)
	}
	
}
