package org.wdt.wdtc.core.auth.accounts

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.auth.BaseUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.FileManger.userJson
import org.wdt.wdtc.core.utils.SkinUtils
import org.wdt.wdtc.core.utils.StringUtils.cleanStrInString
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.IOException
import java.util.*

class OfflineAccounts(private val username: String) : BaseUser() {
  private val logmaker = getLogger(OfflineAccounts::class.java)
  private val userUuid = UUID.randomUUID().toString().cleanStrInString("-")

  @get:Throws(IOException::class)
  override val user: User
    get() {
      val utils = SkinUtils(SkinUtils(username).getSkinFile())
      utils.userSkinInput = OfflineAccounts::class.java.getResourceAsStream("/assets/skin/steve.png")
      val user =
        User(
          username,
          "\${auth_access_token}",
          AccountsType.Offline,
          userUuid,
          headFile = utils.writeSkinHead()
        )
      userJson.writeObjectToFile(user, Json.GSON_BUILDER.setPrettyPrinting())
      logmaker.info(user)
      return user
    }
}
