package org.wdt.wdtc.core.impl.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.download.interfaces.VersionListInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asVersionsObjectSequence
import org.wdt.wdtc.core.openapi.manger.currentDownloadSource
import org.wdt.wdtc.core.openapi.utils.ioAsync
import org.wdt.wdtc.core.openapi.utils.toURL
import java.util.*

class FabricVersionList : VersionListInterface {
	private val versionListUrl = "${currentDownloadSource.fabricMetaUrl}v2/versions/loader"
	
	private val list = ioAsync { versionListUrl.toURL().toStrings().parseJsonArray() }
	
	override suspend fun getVersionList(): VersionsObjectSequence {
		return list.await().asSequence<FabricVersionsJsonObjectImpl>().asVersionsObjectSequence()
	}
	
	class FabricVersionsJsonObjectImpl(
		@field:SerializedName("version")
		override var versionNumber: String,
		
		@field:SerializedName("build")
		val buildNumber: Int = 0
	
	) : VersionsJsonObjectInterface {
		
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
