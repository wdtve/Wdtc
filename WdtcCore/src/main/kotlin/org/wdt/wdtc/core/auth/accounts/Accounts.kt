package org.wdt.wdtc.core.auth.accounts

import org.wdt.wdtc.core.auth.preferredUser
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.utils.STRING_EMPTY
import org.wdt.wdtc.core.utils.STRING_SPACE

object Accounts {
  private val type: AccountsType
  private val user = preferredUser

  init {
    type = user.type
  }

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
      append(user.base64Data)
    }

  enum class AccountsType {
    OFFLINE,
    YGGDRASIL
  }
}
