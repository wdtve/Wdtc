package org.wdt.wdtc.core.impl.download.game

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.openapi.download.interfaces.VersionListInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asSequence
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asVersionsObjectSequence
import org.wdt.wdtc.core.openapi.manager.versionManifestFile
import org.wdt.wdtc.core.openapi.utils.ioAsync
import java.net.URL
import java.util.*

class GameVersionList : VersionListInterface {
	
	private val list = ioAsync {
		versionManifestFile.run {
			if (isFileNotExists()) {
				DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
			}
			readFileToJsonObject().getJsonArray("versions")
		}
	}
	
	override suspend fun getVersionList(): VersionsObjectSequence {
		return list.await().asSequence<GameVersionsJsonObjectImpl>().filter {
			it.gameType == "release"
		}.asVersionsObjectSequence()
	}
	
	
	class GameVersionsJsonObjectImpl(
		@field:SerializedName("id")
		override val versionNumber: String,
		
		@field:SerializedName("releaseTime")
		val releaseTime: Date,
		
		@field:SerializedName("type")
		val gameType: String,
		
		@field:SerializedName("url")
		val versionJsonURL: URL,
		
		@field:SerializedName("time")
		val time: Date
	
	) : VersionsJsonObjectInterface {
		
		
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other == null || javaClass != other.javaClass) return false
			val that = other as GameVersionsJsonObjectImpl
			return versionNumber == that.versionNumber && gameType == that.gameType
		}
		
		override fun hashCode(): Int {
			return Objects.hash(versionNumber, gameType)
		}
	}
	
}