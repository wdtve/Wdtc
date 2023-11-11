package org.wdt.wdtc.core.auth.accounts

import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.manger.FileManger.authlibInjector
import org.wdt.wdtc.core.manger.URLManger.littleskinApi
import org.wdt.wdtc.core.utils.StringUtils
import org.wdt.wdtc.core.utils.StringUtils.appendForString

class Accounts {
	private val type: AccountsType

	init {
		type = if (User.user.type == AccountsType.Offline) AccountsType.Offline else AccountsType.Yggdrasil
	}

	fun ifAccountsIsOffline(): Boolean {
		return type != AccountsType.Yggdrasil
	}

	val jvmCommand: String
		get() = if (ifAccountsIsOffline())
			StringUtils.STRING_EMPTY
		else StringUtils.STRING_SPACE.appendForString(
			"-javaagent:",
			authlibInjector,
			"=",
			littleskinApi,
			StringUtils.STRING_SPACE,
			"-Dauthlibinjector.yggdrasil.prefetched=",
			User.user.base64Data
		)

	enum class AccountsType {
		Offline,
		Yggdrasil
	}
}
