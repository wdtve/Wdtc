package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.manger.FileManger.minecraftComSkin
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.SkinUtils
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class YggdrasilTextures(yggdrasilAccounts: YggdrasilAccounts) {
	val username: String

	@NotNull
	var url: String? = null

	init {
		username = yggdrasilAccounts.username
		url = if (Objects.nonNull(yggdrasilAccounts.url)) {
			yggdrasilAccounts.url
		} else {
			throw NullPointerException("URL为空")
		}
	}

	val userJsonUrl: String
		get() = "$url/csl/$username.json"


	@Throws(IOException::class)
	fun getUserSkinHash(): String {
		val csl = csl
		val skins: Skins = csl.skins!!
		return skins.skinKind!!
	}

	val userSkinFile: File
		get() = SkinUtils(username).getSkinFile()

	@Throws(IOException::class)
	fun startDownloadUserSkin() {
		val userSkinHash = getUserSkinHash()
		val skinPath = File(minecraftComSkin, "${userSkinHash.substring(0, 2)}/$userSkinHash")
		startDownloadTask(skinUrl, skinPath)
	}

	@get:Throws(IOException::class)
	val skinUrl: URL
		get() = URL(url + "/textures/" + getUserSkinHash())

	@get:Throws(IOException::class)
	val csl: Csl
		get() = getURLToString(userJsonUrl).parseObject()

	@get:Throws(IOException::class)
	val utils: SkinUtils
		get() {
			val utils = SkinUtils(userSkinFile)
			utils.userSkinInput = skinUrl.openStream()
			return utils
		}

	class Csl {
		@SerializedName("username")
		private val userName: String? = null

		@SerializedName("skins")
		val skins: Skins? = null

		@SerializedName("cape")
		val cape: String? = null
	}

	class Skins {
		@SerializedName(value = "default", alternate = ["slim"])
		val skinKind: String? = null
	}
}
