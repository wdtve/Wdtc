package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import org.wdt.utils.gson.*
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.auth.LoginUser
import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.littleskinApiUrl
import org.wdt.wdtc.core.utils.ioCoroutineScope
import org.wdt.wdtc.core.utils.noNull
import org.wdt.wdtc.core.utils.toBase64
import java.io.IOException
import java.io.PrintWriter
import java.net.URL

class YggdrasilAccounts(
	val url: String,
	val userName: String,
	private val password: String
) : LoginUser {
	
	@get:Throws(IOException::class)
	private val sendPostWithJson = ioCoroutineScope.async {
		URL("$url/api/yggdrasil/authserver/authenticate").let {
			it.openConnection().apply {
				setRequestProperty("content-type", "application/json")
				doOutput = true
				doInput = true
			}.apply {
				PrintWriter(getOutputStream()).run {
					print(PostJsonObject(userName, password).toJsonString())
					flush()
				}
			}.getInputStream().toStrings().parseObject<UserInformation>()
		}
	}
	
	val yggdrasilTextures: YggdrasilTextures
		get() = YggdrasilTextures(this)
	
	override val user: User
		get() = runBlocking(Dispatchers.IO) {
			val metaData = async { littleskinApiUrl.toStrings() }
			sendPostWithJson.await().run {
				User(
					userName,
					accessToken,
					AccountsType.YGGDRASIL,
					selectedProfile.getString("id"),
					yggdrasilTextures.utils.writeSkinHead()
				).apply {
					this.metaData = metaData.await()
					base64Data = this.metaData.noNull().toBase64()
				}
			}
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
