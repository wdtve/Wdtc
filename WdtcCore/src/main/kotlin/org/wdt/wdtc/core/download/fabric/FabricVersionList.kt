package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FabricVersionList : VersionListInterface {
  @get:Throws(IOException::class)
  override val versionList: List<VersionJsonObjectInterface>
    get() {
      val fabricVersionList: MutableList<VersionJsonObjectInterface> = ArrayList()
      val versionArray = "${downloadSource.fabricMetaUrl}v2/versions/loader".toURL().toStrings().parseJsonArray()
      versionArray.forEach { fabricVersionList.add(it.asJsonObject.parseObject()) }
      return fabricVersionList
    }

  class FabricVersionJsonObjectImpl : VersionJsonObjectInterface {
    @SerializedName("version")
    override val versionNumber: String? = null

    @SerializedName("build")
    var buildNumber = 0

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as FabricVersionJsonObjectImpl
      return buildNumber == that.buildNumber && versionNumber == that.versionNumber
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, buildNumber)
    }
  }
}
