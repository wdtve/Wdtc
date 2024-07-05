package org.wdt.wdtc.core.openapi.game

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.gson.VersionTypeAdapter
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionsListGson
import org.wdt.wdtc.core.openapi.utils.info
import org.wdt.wdtc.core.openapi.utils.logmaker
import java.io.File

interface Version : GameFileManager, GameDirectoryManager {
	val kind: KindOfMod
	val modVersion: String
	val modInstallTask: ModInstallTaskInterface?
}

@JsonAdapter(VersionTypeAdapter::class)
class VersionImpl internal constructor(
	override val versionNumber: String,
	val manager: GameDirectoryManager
) : Version, GameFileManager, GameDirectoryManager by manager {
	
	@JvmOverloads
	constructor(
		versionNumber: String,
		workDirectory: File = currentSetting.defaultGamePath
	) : this(
		versionNumber,
		GameDirectoryManagerImpl(workDirectory)
	)
	
	override val kind: KindOfMod = KindOfMod.ORIGINAL
	
	override val modVersion: String = versionNumber
	
	override val modInstallTask: ModInstallTaskInterface?
		get() = null // game self is not need to install mod
	
	
	override val versionDirectory: File
		get() = File(gameVersionsDirectory, versionNumber)
	
	override val versionJson: File
		get() = File(versionDirectory, "$versionNumber.json")
	
	override val versionJar: File
		get() = File(versionDirectory, "$versionNumber.jar")
	
	override val versionLog4j2: File
		get() = File(versionDirectory, "log4j2.xml")
	
	override val versionNativesPath: File
		get() = File(versionDirectory, "natives-windows")
	
	override val gameAssetsListJson: File
		get() = File(gameAssetsDirectory, "indexes/${gameVersionJsonObject.assets}.json")
	
	override val gameOptionsFile: File
		get() = File(versionDirectory, "options.txt")
	
	override val gameModsPath: File
		get() = File(versionDirectory, "mods")
	
	override val gameLogDirectory: File
		get() = File(versionDirectory, "logs")
	
	override val versionConfigFile: File
		get() = File(versionDirectory, "config.json")
	
	override val laucnherProfiles: File
		get() = File(gameDirectory, "Launcher_profiles.json")
	
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Version) return false
		
		if (versionNumber != other.versionNumber) return false
		if (kind != other.kind) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = versionNumber.hashCode()
		result = 31 * result + kind.hashCode()
		return result
	}
	
	override fun toString(): String {
		return "VersionImpl(versionNumber='$versionNumber', manager=$manager, kind=$kind, modVersion='$modVersion')"
	}
	
	
}

fun Version.toVersionImpl(): VersionImpl {
	return VersionImpl(versionNumber, workDirectory)
}

val preferredVersion: Version?
	get() = currentSetting.preferredVersion?.takeIf { it.ckeckIsEffective() }

class VersionsList(
	private val versions: MutableSet<Version> = hashSetOf()
) : MutableSet<Version> by versions {
	
	companion object {
		fun VersionsList.saveChangeToFile() {
			versionsJson.writeObjectToFile(serializeVersionsListGson) { this }
		}
		
		inline fun VersionsList.changeListToFile(block: VersionsList.() -> Unit): VersionsList {
			return apply(block).also { it.saveChangeToFile() }
		}
	}
}

val currentVersionsList: VersionsList
	get() = versionsJson.readFileToClass(serializeVersionsListGson)

fun printVersionList() {
	currentVersionsList.forEach { logmaker.info(it) }
}