package org.wdt.wdtc.core.auth.accounts

import org.wdt.wdtc.core.auth.preferredUser
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.utils.STRING_EMPTY
import org.wdt.wdtc.core.utils.STRING_SPACE
import org.wdt.wdtc.core.utils.appendForString

class Accounts {
  private val type: AccountsType

  init {
    type = if (preferredUser.type == AccountsType.Offline) AccountsType.Offline else AccountsType.Yggdrasil
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
      preferredUser.base64Data
    )

  enum class AccountsType {
    Offline,
    Yggdrasil
  }
}
