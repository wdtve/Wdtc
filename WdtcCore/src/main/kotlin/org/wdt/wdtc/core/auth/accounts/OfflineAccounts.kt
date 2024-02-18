package org.wdt.wdtc.core.auth.accounts

import org.wdt.wdtc.core.auth.LoginUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.utils.SkinUtils
import org.wdt.wdtc.core.utils.cleanStrInString
import org.wdt.wdtc.core.utils.getResourceAsStream
import java.util.*

class OfflineAccounts(private val username: String) : LoginUser {
  override val user: User
    get() {
      val utils = SkinUtils(SkinUtils.getUserSkinFile(username)).apply {
        userSkinInput = getResourceAsStream("/assets/skin/steve.png")
      }
      return User(
        username,
        "\${auth_access_token}",
        AccountsType.OFFLINE,
        UUID.randomUUID().toString().cleanStrInString("-"),
        utils.writeSkinHead()
      )
    }
}
