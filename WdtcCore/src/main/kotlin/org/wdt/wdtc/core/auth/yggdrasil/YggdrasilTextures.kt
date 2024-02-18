package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.manger.isWindows
import org.wdt.wdtc.core.manger.minecraftComSkin
import org.wdt.wdtc.core.utils.SkinUtils
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class YggdrasilTextures(yggdrasilAccounts: YggdrasilAccounts) {
  private val userName: String = yggdrasilAccounts.userName
  private val url: String = yggdrasilAccounts.url


  private val userJsonUrl: URL = "$url/csl/$userName.json".toURL()

  private val userSkinHash: String
    get() = csl.skins?.skinKind.ckeckIsNull()

  private val userSkinFile: File = SkinUtils.getUserSkinFile(userName)

  @Throws(IOException::class)
  fun startDownloadUserSkin() {
    if (!isWindows) return
    val skinPath = userSkinHash.let {
      File(minecraftComSkin, "${it.substring(0, 2)}/$it")
    }
    startDownloadTask(skinUrl, skinPath)
  }

  @get:Throws(IOException::class)
  val skinUrl: URL
    get() = URL("$url/textures/${userSkinHash}")

  private val csl: Csl
    get() = userJsonUrl.toStrings().parseObject()

  @get:Throws(IOException::class)
  val utils: SkinUtils
    get() = SkinUtils(userSkinFile).apply {
      userSkinInput = skinUrl.openStream()
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
