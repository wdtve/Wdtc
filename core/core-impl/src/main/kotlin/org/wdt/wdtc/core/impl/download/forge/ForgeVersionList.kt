package org.wdt.wdtc.core.impl.download.forge

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.download.interfaces.VersionListInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asVersionsObjectSequence
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.ioAsync
import org.wdt.wdtc.core.openapi.utils.toURL
import java.util.*

class ForgeVersionList(version: Version) : VersionListInterface {
	private val forgeListUrl = "https://bmclapi2.bangbang93.com/forge/minecraft/${version.versionNumber}"
	
	private val list = ioAsync { forgeListUrl.toURL().toStrings().parseJsonArray() }
	
	override suspend fun getVersionList(): VersionsObjectSequence {
		return list.await().asSequence<ForgeVersionsJsonObjectImpl>().asVersionsObjectSequence()
	}
	
	
	class ForgeVersionsJsonObjectImpl(
		@field:SerializedName("version") override val versionNumber: String,
		
		@field:SerializedName("modified") val modified: Date,
	) : VersionsJsonObjectInterface {
		
		@SerializedName("mcversion")
		val mcversion: String? = null
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other == null || javaClass != other.javaClass) return false
			val that = other as ForgeVersionsJsonObjectImpl
			return versionNumber == that.versionNumber && mcversion == that.mcversion
		}
		
		override fun hashCode(): Int {
			return Objects.hash(versionNumber, mcversion)
		}
	}
	
}
