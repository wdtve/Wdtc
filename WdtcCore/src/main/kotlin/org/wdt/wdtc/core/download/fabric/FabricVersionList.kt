package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.manger.currentDownloadSource
import org.wdt.wdtc.core.utils.toURL
import java.util.*

class FabricVersionList : VersionListInterface {
	private val versionListUrl = "${currentDownloadSource.fabricMetaUrl}v2/versions/loader"
	
	override val versionList: LinkedList<VersionsJsonObjectInterface>
		get() = LinkedList<VersionsJsonObjectInterface>().apply {
      versionListUrl.toURL().toStrings().parseJsonArray().forEach {
        add(it.asJsonObject.parseObject<FabricVersionsJsonObjectImpl>())
      }
    }


  class FabricVersionsJsonObjectImpl(
    @field:SerializedName("version")
    override val versionNumber: String
  ) : VersionsJsonObjectInterface {

    @SerializedName("build")
    val buildNumber = 0
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as FabricVersionsJsonObjectImpl
      return buildNumber == that.buildNumber && versionNumber == that.versionNumber
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, buildNumber)
    }


  }
}
