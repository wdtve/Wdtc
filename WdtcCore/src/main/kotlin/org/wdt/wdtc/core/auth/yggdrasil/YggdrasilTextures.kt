package org.wdt.wdtc.core.auth.yggdrasil

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.manger.minecraftComSkin
import org.wdt.wdtc.core.utils.SkinUtils
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class YggdrasilTextures(yggdrasilAccounts: YggdrasilAccounts) {
  private val userName: String = yggdrasilAccounts.userName

  @NotNull
  var url: String? = null

  init {
    url = yggdrasilAccounts.url
  }

  private val userJsonUrl: URL = "$url/csl/$userName.json".toURL()

  @Throws(IOException::class)
  fun getUserSkinHash(): String {
    val skins: Skins? = csl.skins
    return skins?.skinKind ?: throw NullPointerException()
  }

  private val userSkinFile: File = SkinUtils.getUserSkinFile(userName)

  @Throws(IOException::class)
  fun startDownloadUserSkin() {
    val userSkinHash = getUserSkinHash()
    val skinPath = File(minecraftComSkin, "${userSkinHash.substring(0, 2)}/$userSkinHash")
    startDownloadTask(skinUrl, skinPath)
  }

  @get:Throws(IOException::class)
  val skinUrl: URL
    get() = URL(url + "/textures/" + getUserSkinHash())

  private val csl: Csl
    get() = userJsonUrl.toStrings().parseObject()

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
