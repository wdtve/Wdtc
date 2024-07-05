package org.wdt.wdtc.core.impl.auth.accounts.yggdrasil

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.manager.isWindows
import org.wdt.wdtc.core.openapi.manager.minecraftComSkin
import org.wdt.wdtc.core.openapi.utils.SkinUtils
import org.wdt.wdtc.core.openapi.utils.newInputStream
import org.wdt.wdtc.core.openapi.utils.startDownloadTask
import org.wdt.wdtc.core.openapi.utils.toURL
import java.io.File
import java.net.URL

class YggdrasilTextures(
	private val userName: String,
	private val url: String,
) {
	
	private val userSkinHash: String
		get() = "$url/csl/$userName.json".toURL().toStrings().parseObject<Csl>().skins.skinKind
	
	
	suspend fun startDownloadUserSkin() {
		if (!isWindows) return
		val skinPath = userSkinHash.let {
			File(minecraftComSkin, "${it.substring(0, 2)}/$it")
		}
		startDownloadTask(skinUrl to skinPath)
	}
	
	private val skinUrl: URL
		get() = URL("$url/textures/${userSkinHash}")
	
	val utils: SkinUtils
		get() = SkinUtils(SkinUtils.getUserSkinFile(userName)).apply {
			userSkinInput = skinUrl.newInputStream()
		}
	
	class Csl(
		@field:SerializedName("username")
		private val userName: String,
		@field:SerializedName("skins")
		val skins: Skins,
		@field:SerializedName("cape")
		val cape: String
	)
	
	class Skins(
		@field:SerializedName(value = "default", alternate = ["slim"])
		val skinKind: String
	)
}
