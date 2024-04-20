package org.wdt.wdtc.core.impl.download.fabric

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
import java.net.URL
import java.util.*

class FabricAPIVersionList(private val version: Version) : VersionListInterface {
	
	private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()
	
	private val list = ioAsync { versionListUrl.toStrings().parseJsonArray() }
	
	override suspend fun getVersionList(): VersionsObjectSequence {
		return list.await().asSequence<FabricAPIVersionsJsonObjectImpl>().filter {
			version.versionNumber == it.gameVersion.first()
		}.asVersionsObjectSequence()
	}
}


class FabricAPIVersionsJsonObjectImpl(
	@field:SerializedName("version_number")
	override val versionNumber: String,
	
	@field:SerializedName("date_published")
	val releaseTime: Date,
	
	@field:SerializedName("game_versions")
	val gameVersion: List<String>,
	
	@field:SerializedName("files")
	val files: List<FilesObject>,
	
	) : VersionsJsonObjectInterface {
	
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as FabricAPIVersionsJsonObjectImpl
		return versionNumber == that.versionNumber
	}
	
	override fun hashCode(): Int {
		return Objects.hash(versionNumber)
	}
	
	
	class FilesObject(
		@field:SerializedName("url")
		val url: URL,
		@field:SerializedName("size")
		val size: Int,
		@field:SerializedName("filename")
		val name: String
	)
}
