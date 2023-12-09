package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.*
import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.auth.BaseUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.manger.userJson
import org.wdt.wdtc.core.utils.getURLToString
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.toBase64
import java.io.IOException
import java.io.PrintWriter
import java.net.URL

class YggdrasilAccounts(val url: String, val username: String, val password: String) : BaseUser() {

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
      val selectedProfile = userInfo.selectedProfile!!
      val api = littleskinApiUrl.getURLToString()
      val user = User(
        username,
        userInfo.accessToken,
        AccountsType.Yggdrasil,
        selectedProfile.getString("id"),
        api,
        api.toBase64(),
        textures.utils.writeSkinHead()
      )
      userJson.writeObjectToFile(user, Json.GSON_BUILDER.setPrettyPrinting())
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
    private val agent: Agent = Agent()

    class Agent @JvmOverloads constructor(
      @field:SerializedName("name")
      private val name: String = "Minecraft",

      @field:SerializedName("version")
      private val version: Int = 1
    )

  }

  class UserInformation {
    var accessToken: String? = null
    var clientToken: String? = null
    var availableProfiles: JsonArray? = null
    var user: JsonObject? = null
    var selectedProfile: JsonObject? = null
  }
}
