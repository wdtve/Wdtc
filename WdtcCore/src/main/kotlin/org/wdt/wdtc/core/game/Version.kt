package org.wdt.wdtc.core.game

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.game.VersionsList.Companion.changeListToFile
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.gson.serializeVersionsListGson
import org.wdt.wdtc.core.utils.info
import org.wdt.wdtc.core.utils.logmaker
import java.io.File

class Version(
	versionNumber: String,
	here: File = currentSetting.defaultGamePath
) : GameFileManger(versionNumber, here) {
	
	var kind = KindOfMod.ORIGINAL
	
	var fabricModInstallInfo: FabricDonwloadInfo? = null
		set(value) {
			kind = KindOfMod.FABRIC
			field = value
		}
	
	var forgeModDownloadInfo: ForgeDownloadInfo? = null
		set(value) {
			kind = KindOfMod.FORGE
			field = value
		}
	
	
	var quiltModDownloadInfo: QuiltDownloadInfo? = null
		set(value) {
			kind = KindOfMod.QUILT
			field = value
		}
	
	
	fun cleanKind() {
		kind = KindOfMod.ORIGINAL
	}
	
	fun ckeckIsEffective(): Boolean {
		return if (versionConfigFile.isFileExists()) {
			gameConfig.configVersion == this
		} else false
	}
	
	
	override fun toString(): String {
		return "Version(kind=$kind, fabricModInstallInfo=$fabricModInstallInfo, forgeModDownloadInfo=$forgeModDownloadInfo, quiltModDownloadInfo=$quiltModDownloadInfo)"
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Version) return false
		if (!super.equals(other)) return false
		
		if (kind != other.kind) return false
		if (fabricModInstallInfo != other.fabricModInstallInfo) return false
		if (forgeModDownloadInfo != other.forgeModDownloadInfo) return false
		if (quiltModDownloadInfo != other.quiltModDownloadInfo) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + kind.hashCode()
		result = 31 * result + (fabricModInstallInfo?.hashCode() ?: 0)
		result = 31 * result + (forgeModDownloadInfo?.hashCode() ?: 0)
		result = 31 * result + (quiltModDownloadInfo?.hashCode() ?: 0)
		return result
	}
	
}

val preferredVersion: Version?
	get() = currentSetting.preferredVersion?.takeIf { it.ckeckIsEffective() }
class VersionsList(
	private val versions: LinkedHashSet<Version> = linkedSetOf()
) : MutableSet<Version> by versions {
	fun Version.addToList(): Boolean = add(this)
	
	
	companion object {
		fun VersionsList.saveChangeToFile() {
			versionsJson.writeObjectToFile(this, serializeVersionsListGson)
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

fun ckeckVersionsList() {
	GameDirectoryManger().gameVersionList.forEach {
		if (it !in currentVersionsList) {
			currentVersionsList.changeListToFile {
				it.gameConfig.configVersion.addToList()
			}
		}
	}
	removeInvalidVersion()
}

private fun removeInvalidVersion() {
	currentVersionsList.run {
		try {
			forEach {
				if (!it.ckeckIsEffective()) {
					logmaker.info("$it invalid")
					changeListToFile { remove(it) }
				}
			}
		} catch (_: ConcurrentModificationException) {
			removeInvalidVersion()
		}
	}
	
}
