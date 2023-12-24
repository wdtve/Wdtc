package org.wdt.wdtc.core.auth.accounts

import org.wdt.wdtc.core.auth.getPreferredUser
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.utils.STRING_EMPTY
import org.wdt.wdtc.core.utils.STRING_SPACE
import org.wdt.wdtc.core.utils.appendForString

class Accounts {
  private val type: AccountsType
  private val user = getPreferredUser()

  init {
    type = if (user.type == AccountsType.Offline) AccountsType.Offline else AccountsType.Yggdrasil
  }

  private val ifAccountsIsOffline = type != AccountsType.Yggdrasil

  val jvmCommand: String
    get() = if (ifAccountsIsOffline)
      STRING_EMPTY
    else STRING_SPACE.appendForString(
      "-javaagent:",
      authlibInjector,
      "=",
      littleskinApiUrl,
      STRING_SPACE,
      "-Dauthlibinjector.yggdrasil.prefetched=",
      user.base64Data
    )

  enum class AccountsType {
    Offline,
    Yggdrasil
  }
}
