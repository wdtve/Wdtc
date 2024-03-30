package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionsJsonObjectImpl
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionsJsonObjectImpl.FilesObject
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.noNull
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File

class FabricAPIDownloadTask(private val version: Version, val fabricAPIVersionNumber: String?) {
	
	private var versionsJsonObjectInterface: VersionsJsonObjectInterface? = null
	private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version,
		versionsJsonObjectInterface.versionNumber
	) {
		this.versionsJsonObjectInterface = versionsJsonObjectInterface
	}
	
	suspend fun startDownloadFabricAPI() {
		val versionJsonObject = versionsJsonObjectInterface as FabricAPIVersionsJsonObjectImpl?
		if (versionJsonObject == null) {
			versionListUrl.toStrings().parseJsonArray().forEach {
				it.asJsonObject.parseObject<FabricAPIVersionsJsonObjectImpl>().also { api ->
					if (api.versionNumber == fabricAPIVersionNumber) {
						downloadFabricAPITask(api.filesObjectList.noNull().first())
					}
				}
			}
		} else {
			downloadFabricAPITask(versionJsonObject.filesObjectList.noNull().first())
		}
	}
	
	private suspend fun downloadFabricAPITask(filesObject: FilesObject) {
		startDownloadTask(filesObject.jarDownloadURL, File(version.gameModsPath, filesObject.jarFileName))
	}
	
	override fun toString(): String {
		return "FabricAPIDownloadTask(fabricAPIVersionNumber=$fabricAPIVersionNumber)"
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is FabricAPIDownloadTask) return false
		
		if (fabricAPIVersionNumber != other.fabricAPIVersionNumber) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		return fabricAPIVersionNumber?.hashCode() ?: 0
	}
	
	
}
