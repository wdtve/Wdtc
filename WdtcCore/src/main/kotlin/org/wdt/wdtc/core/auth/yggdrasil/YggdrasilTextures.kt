package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.manger.isWindows
import org.wdt.wdtc.core.manger.minecraftComSkin
import org.wdt.wdtc.core.utils.*
import java.io.File
import java.net.URL

class YggdrasilTextures(
	private val userName: String,
	private val url: String,
) {
	
	private val userSkinHash: String
		get() = "$url/csl/$userName.json".toURL().toStrings().parseObject<Csl>().skins?.skinKind.noNull()
	
	
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
