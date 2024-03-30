package org.wdt.wdtc.core.download.quilt

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.officialDownloadSource
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.util.*

class QuiltVersionList(private val version: Version) : VersionListInterface {
	private val quiltquVersionListUrl = "${officialDownloadSource.quiltMetaUrl}v3/versions/loader/%s"
	
	@get:Throws(IOException::class)
	override val versionList: LinkedList<VersionsJsonObjectInterface>
		get() {
			return LinkedList<VersionsJsonObjectInterface>().apply {
				quiltquVersionListUrl.format(version.versionNumber).toURL().toStrings().parseJsonArray().forEach {
					add(it.asJsonObject.getJsonObject("loader").parseObject<QuiltVersionsJsonObjectImpl>())
				}
			}
		}
	
	class QuiltVersionsJsonObjectImpl(
		@field:SerializedName("version")
		override val versionNumber: String
	) : VersionsJsonObjectInterface {
		
		@SerializedName("build")
		var buildNumber = 0
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other == null || javaClass != other.javaClass) return false
			val that = other as QuiltVersionsJsonObjectImpl
			return buildNumber == that.buildNumber && versionNumber == that.versionNumber
		}
		
		override fun hashCode(): Int {
			return Objects.hash(versionNumber, buildNumber)
		}
		
		
	}
	
}
