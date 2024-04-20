package org.wdt.wdtc.core.impl.download.fabric

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToClass
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.serializeGameRuntimeDependencyGsonBuilder
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import org.wdt.wdtc.core.openapi.manger.officialDownloadSource
import org.wdt.wdtc.core.openapi.manger.wdtcCache
import org.wdt.wdtc.core.openapi.utils.gson.DownloadInfoTypeAdapter
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.startDownloadTask
import org.wdt.wdtc.core.openapi.utils.toURL
import java.io.File
import java.net.URL

@JsonAdapter(DownloadInfoTypeAdapter::class)
open class FabricDonwloadInfo(
	protected val version: Version,
	override val modVersion: String
) : ModDownloadInfoInterface {
	var apiDownloadTask: FabricAPIDownloadTask? = null
	
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
		version,
		versionsJsonObjectInterface.versionNumber.noNull()
	)
	
	val fabricVersionFileUrl: String
		get() = "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/json"
	
	suspend fun startDownloadProfileZip() {
		startDownloadTask(profileZipUrl, profileZipFile)
	}
	
	
	private val profileZipFile: File
		get() = File(wdtcCache, "${version.versionNumber}-$modVersion-frofile-zip.zip")
	
	private val profileZipUrl: URL
		get() = "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/zip".format(
			version.versionNumber,
			modVersion
		).toURL()
	
	
	private val fromFabricLoaderFolder: String
		get() = "fabric-loader-%s-%s".format(modVersion, version.versionNumber)
	
	
	val fabricJar: File
		get() = File(wdtcCache, "$fromFabricLoaderFolder/$fromFabricLoaderFolder.jar")
	val fabricVersionJson: File
		get() = File(wdtcCache, "${version.versionNumber}-fabric-$modVersion.json")
	
	val fabricProfileJsonObject: FabricProfileJsonObject
		get() = fabricVersionJson.readFileToClass(serializeGameRuntimeDependencyGsonBuilder)
	val isHasApiDownloadTask: Boolean
		get() = apiDownloadTask != null
	override val modInstallTask: ModInstallTaskInterface
		get() = FabricInstallTask(version, modVersion)
	override val modKind: KindOfMod
		get() = KindOfMod.FABRIC
	
	override fun toString(): String {
		return "FabricDonwloadInfo(modVersion='$modVersion', apiDownloadTask=$apiDownloadTask)"
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is FabricDonwloadInfo) return false
		
		if (modVersion != other.modVersion) return false
		if (apiDownloadTask != other.apiDownloadTask) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = modVersion.hashCode()
		result = 31 * result + (apiDownloadTask?.hashCode() ?: 0)
		return result
	}
	
	
}
