package org.wdt.wdtc.core.download.forge

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.net.URL
import java.util.*

class ForgeVersionList(private val launcher: Launcher) : VersionListInterface {
  private val forgeListUrl: URL
    get() = "https://bmclapi2.bangbang93.com/forge/minecraft/${launcher.versionNumber}".toURL()

  @get:Throws(IOException::class)
  override val versionList: List<VersionJsonObjectInterface>
    get() {
      val versionObjects: MutableList<VersionJsonObjectInterface> = ArrayList()
      val versionList = forgeListUrl.toStrings().parseJsonArray()
      versionList.forEach { versionObjects.add(it.asJsonObject.parseObject()) }
      return versionObjects
    }

  class ForgeVersionJsonObjectImpl : VersionJsonObjectInterface {
    @SerializedName("version")
    override val versionNumber: String? = null

    @SerializedName("modified")
    val modified: String? = null

    @SerializedName("mcversion")
    val mcversion: String? = null

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as ForgeVersionJsonObjectImpl
      return versionNumber == that.versionNumber && mcversion == that.mcversion
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, mcversion)
    }
  }
}
