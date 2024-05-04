package org.wdt.wdtc.core.impl.auth.accounts.yggdrasil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import okio.ByteString.Companion.encodeUtf8
import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.toJsonString
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.auth.Accounts
import org.wdt.wdtc.core.openapi.auth.LoginUser
import org.wdt.wdtc.core.openapi.auth.User
import org.wdt.wdtc.core.openapi.manger.littleskinApiUrl
import org.wdt.wdtc.core.openapi.utils.ioAsync
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.runOnIO
import java.io.File
import java.io.PrintWriter
import java.net.URL

class YggdrasilAccounts(
	val url: String,
	private val userName: String,
	private val password: String
) : LoginUser {
	
	val textures = YggdrasilTextures(userName, url)
	
	private val sendPostWithJson = ioAsync {
		URL("$url/api/yggdrasil/authserver/authenticate").openConnection().apply {
			setRequestProperty("content-type", "application/json")
			doOutput = true
			doInput = true
		}.also {
			PrintWriter(it.outputStream).apply {
				print(PostJsonObject(userName, password).toJsonString())
				flush()
			}
		}.inputStream.use { input ->
			input.toStrings().parseObject<UserInformation>()
		}
	}
	
	private val headFile: Deferred<File> = ioAsync {
		textures.utils.writeSkinHead()
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
	
	override suspend fun getUser(): User = runOnIO {
		val metaDataDeferred = async { littleskinApiUrl.toStrings() }
		sendPostWithJson.await().let {
			User(
				userName, it.accessToken, Accounts.AccountsType.YGGDRASIL, it.selectedProfile.getString("id"), headFile.await()
			).apply {
				this.metaData = metaDataDeferred.await()
				this.base64Data = metaData.noNull().encodeUtf8().base64()
			}
		}
	}
}
