package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.utils.getURLToString
import java.io.IOException
import java.util.*

class FabricVersionList : VersionListInterface {
  @get:Throws(IOException::class)
  override val versionList: Set<VersionJsonObjectInterface>
    get() {
      val fabricVersionList: MutableSet<VersionJsonObjectInterface> = HashSet()
      val versionArray = "${downloadSource.fabricMetaUrl}v2/versions/loader".getURLToString().parseJsonArray()
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
