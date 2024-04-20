package org.wdt.wdtc.core.impl.download.forge

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToClass
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.serializeGameRuntimeDependencyGsonBuilder
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import org.wdt.wdtc.core.openapi.manger.currentDownloadSource
import org.wdt.wdtc.core.openapi.manger.wdtcCache
import org.wdt.wdtc.core.openapi.utils.gson.DownloadInfoTypeAdapter
import org.wdt.wdtc.core.openapi.utils.startDownloadTask
import org.wdt.wdtc.core.openapi.utils.toURL
import org.wdt.wdtc.core.openapi.utils.unZipToFile
import java.io.File
import java.net.URL

// TODO Using 'Url' class
@JsonAdapter(DownloadInfoTypeAdapter::class)
open class ForgeDownloadInfo(protected val version: Version, override val modVersion: String) :
	ModDownloadInfoInterface {
	
	
	constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) :
		this(version, versionsJsonObjectInterface.versionNumber)
	
	suspend fun startDownloadInstallJar() {
		startDownloadTask(forgeInstallJarUrl, forgeInstallJarFile)
	}
	
	val forgeInstallJarFile: File
		get() = File(wdtcCache, "$modVersion-installer.jar")
	private val forgeInstallJarUrl: URL
		get() = version.versionNumber.let {
			"${currentDownloadSource.forgeLibraryMavenUrl}net/minecraftforge/forge/${it}-$modVersion/forge-$it-$modVersion-installer.jar".toURL()
		}
	
	fun unZipToInstallProfile() {
		unZipToFile(forgeInstallJarFile, forgeInstallProfileFile, "install_profile.json")
	}
	
	val forgeInstallProfileFile: File
		get() = File(wdtcCache, "install_profile-${version.versionNumber}-$modVersion.json")
	
	val forgeVersionJsonFile: File
		get() = File(wdtcCache, "version-${version.versionNumber}-$modVersion.json")
	
	fun unZipToForgeVersionJson() {
		unZipToFile(forgeInstallJarFile, forgeVersionJsonFile, "version.json")
	}
	
	val forgeVersionJsonObject: ForgeVersionJsonObject
		get() = forgeVersionJsonFile.readFileToClass(forgeJsonObjectGsonBuilder)
	
	val forgeInstallProfileJsonObject: ForgeInstallProfileJsonObject
		get() = forgeInstallProfileFile.readFileToClass(serializeGameRuntimeDependencyGsonBuilder)
	
	override val modInstallTask: ModInstallTaskInterface
		get() = ForgeInstallTask(version, modVersion)
	override val modKind: KindOfMod
		get() = KindOfMod.FORGE
	
	override fun toString(): String {
		return "ForgeDownloadInfo(modVersion='$modVersion')"
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is ForgeDownloadInfo) return false
		
		if (modVersion != other.modVersion) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		return modVersion.hashCode()
	}
	
	
}
