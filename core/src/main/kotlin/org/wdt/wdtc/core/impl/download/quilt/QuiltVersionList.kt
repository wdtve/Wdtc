package org.wdt.wdtc.core.impl.download.quilt

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.download.interfaces.VersionListInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asVersionsObjectSequence
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.officialDownloadSource
import org.wdt.wdtc.core.openapi.utils.ioAsync
import org.wdt.wdtc.core.openapi.utils.toURL
import java.util.*

class QuiltVersionList(version: Version) : VersionListInterface {
	private val quiltquVersionListUrl =
		"${officialDownloadSource.quiltMetaURL}v3/versions/loader/%s".format(version.versionNumber)
	
	private val list = ioAsync { quiltquVersionListUrl.toURL().toStrings().parseJsonArray() }
	
	override suspend fun getVersionList(): VersionsObjectSequence {
		return list.await().asSequence().map {
			it.asJsonObject.getJsonObject("loader").parseObject<QuiltVersionsJsonObjectImpl>()
		}.asVersionsObjectSequence()
	}
	
	class QuiltVersionsJsonObjectImpl(
		@field:SerializedName("version")
		override val versionNumber: String,
		@field:SerializedName("build")
		var buildNumber: Int = 0
	) : VersionsJsonObjectInterface {
		
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
