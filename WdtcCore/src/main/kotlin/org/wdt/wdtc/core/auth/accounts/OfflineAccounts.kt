package org.wdt.wdtc.core.auth.accounts

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
    private val userUuid = UUID.randomUUID().toString().cleanStrInString("-")

    @get:Throws(IOException::class)
    override val user: User
        get() {
            val user = User()
            user.userName = username
            user.type = AccountsType.Offline
            user.accessToken = "\${auth_access_token}"
            user.uuid = userUuid
            val utils = SkinUtils(utils.getSkinFile())
            utils.userSkinInput = OfflineAccounts::class.java.getResourceAsStream("/assets/skin/steve.png")
            user.headFile = utils.writeSkinHead()
            userJson.writeObjectToFile(user)
            logmaker.info(user)
            return user
        }
    val utils: SkinUtils
        get() = SkinUtils(username)

    companion object {
        private val logmaker = getLogger(OfflineAccounts::class.java)
    }
}
