package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.toJsonString
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.auth.BaseUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.FileManger.userJson
import org.wdt.wdtc.core.manger.URLManger.littleskinApi
import org.wdt.wdtc.core.utils.StringUtils.toBase64
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.IOException
import java.io.PrintWriter
import java.net.URL

class YggdrasilAccounts(val url: String, val username: String, val password: String) : BaseUser() {
    private val logmaker = getLogger(YggdrasilAccounts::class.java)

    @Throws(IOException::class)
    fun sendPostWithJson(): String {
        val requestUrl = URL("$url/api/yggdrasil/authserver/authenticate")
        val jsonObject = PostJsonObject(username, password)
        val jsonStr: String = jsonObject.toJsonString()
        val conn = requestUrl.openConnection()
        conn.setRequestProperty("content-type", "application/json")
        conn.setDoOutput(true)
        conn.setDoInput(true)
        val out = PrintWriter(conn.getOutputStream())
        out.print(jsonStr)
        out.flush()
        return IOUtils.toString(conn.getInputStream())
    }

    @get:Throws(IOException::class)
    val userInformation: UserInformation = sendPostWithJson().parseObject()
    val yggdrasilTextures: YggdrasilTextures
        get() = YggdrasilTextures(this)

    @get:Throws(IOException::class)
    override val user: User
        get() {
            val userInfo = userInformation
            val textures = yggdrasilTextures
            val user = User()
            user.type = AccountsType.Yggdrasil
            val selectedProfile = userInfo.selectedProfile!!
            user.userName = selectedProfile.getString("name")
            user.uuid = selectedProfile.getString("id")
            user.accessToken = userInfo.accessToken
            user.metaData = getURLToString(littleskinApi)
            user.base64Data = user.metaData!!.toBase64()
            val utils = textures.utils
            user.headFile = utils.writeSkinHead()
            userJson.writeObjectToFile(user)
            logmaker.info(user)
            return user
        }

    class PostJsonObject(
        @field:SerializedName("username") private val username: String,
        @field:SerializedName("password") private val password: String
    ) {
        @SerializedName("requestUser")
        private val requestUser = true

        @SerializedName("agent")
        private val agent: Agent

        init {
            agent = Agent()
        }

        class Agent {
            @SerializedName("name")
            private val name = "Minecraft"

            @SerializedName("version")
            private val version = 1
        }
    }

    class UserInformation {
        var accessToken: String? = null
        var clientToken: String? = null
        var availableProfiles: JsonArray? = null
        var user: JsonObject? = null
        var selectedProfile: JsonObject? = null
    }
}
