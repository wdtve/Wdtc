package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.*
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.manger.userJson
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.toBase64
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.io.PrintWriter
import java.net.URL

class YggdrasilAccounts(
  val url: String,
  val userName: String,
  private val password: String
) {

  @Throws(IOException::class)
  fun sendPostWithJson(): String {
    val requestUrl = URL("$url/api/yggdrasil/authserver/authenticate")
    val jsonObject = PostJsonObject(userName, password)
    val jsonStr = jsonObject.toJsonString(Json.getBuilder().setPrettyPrinting())
    val conn = requestUrl.openConnection()
    conn.setRequestProperty("content-type", "application/json")
    conn.doOutput = true
    conn.doInput = true
    val out = PrintWriter(conn.getOutputStream())
    out.print(jsonStr)
    out.flush()
    return IOUtils.toString(conn.getInputStream())
  }

  @get:Throws(IOException::class)
  val userInformation: UserInformation = sendPostWithJson().parseObject()
  val yggdrasilTextures: YggdrasilTextures
    get() = YggdrasilTextures(this)

  val user: User
    get() {
      val userInfo = userInformation
      val textures = yggdrasilTextures
      val selectedProfile = userInfo.selectedProfile!!
      val api = littleskinApiUrl.toURL().toStrings()
      val user = User(
        userName,
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

    class Agent(
      @field:SerializedName("name")
      private val name: String = "Minecraft",

      @field:SerializedName("version")
      private val version: Int = 1
    )
  }

  class UserInformation(
    var accessToken: String,
    var clientToken: String,
    var availableProfiles: JsonArray,
    var user: JsonObject,
    var selectedProfile: JsonObject,
  )
}
