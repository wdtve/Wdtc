package org.wdt.wdtc.core.openapi.auth

import org.wdt.wdtc.core.openapi.manger.authlibInjector
import org.wdt.wdtc.core.openapi.manger.littleskinApiUrl
import org.wdt.wdtc.core.openapi.utils.STRING_EMPTY
import org.wdt.wdtc.core.openapi.utils.STRING_SPACE

class Accounts {
	private val type: AccountsType = preferredUser.type
  
  private val ifAccountsIsOffline = type != AccountsType.YGGDRASIL

  val jvmCommand: String
    get() = if (ifAccountsIsOffline)
      STRING_EMPTY
    else buildString {
      append(STRING_SPACE)
      append("-javaagent:")
      append(authlibInjector.canonicalPath)
      append("=")
      append(littleskinApiUrl)
      append(STRING_SPACE)
      append("-Dauthlibinjector.yggdrasil.prefetched=")
	    append(preferredUser.base64Data)
    }

  enum class AccountsType {
    OFFLINE,
    YGGDRASIL
  }
}
