package org.wdt.wdtc.core.impl.download.fabric

import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.impl.download.fabric.FabricAPIVersionsJsonObjectImpl.FilesObject
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence.Companion.asSequence
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.startDownloadTask
import org.wdt.wdtc.core.openapi.utils.toURL

class FabricAPIDownloadTask(private val version: Version, val fabricAPIVersionNumber: String?) {
	
	private var versionsJsonObjectInterface: VersionsJsonObjectInterface? = null
	private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()
	
	private val versionsList by lazy {
		versionListUrl.toStrings().parseJsonArray().asSequence<FabricAPIVersionsJsonObjectImpl>()
	}
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version,
		versionsJsonObjectInterface.versionNumber
	) {
		this.versionsJsonObjectInterface = versionsJsonObjectInterface
	}
	
	suspend fun startDownloadFabricAPI() {
		(versionsJsonObjectInterface as? FabricAPIVersionsJsonObjectImpl).let { api ->
			if (api == null) {
				versionsList.forEach {
					if (it.versionNumber == fabricAPIVersionNumber) {
						it.files.noNull().first().downloadFabricAPITask()
					}
				}
			} else {
				api.files.noNull().first().downloadFabricAPITask()
			}
		}
	}
	
	private suspend fun FilesObject.downloadFabricAPITask() {
		startDownloadTask(url, version.gameModsPath.resolve(name))
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
