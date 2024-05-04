package org.wdt.wdtc.core.openapi.game

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.manger.*
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionsListGson
import org.wdt.wdtc.core.openapi.utils.info
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.noNull
import java.io.File

class Version(
	versionNumber: String,
	here: File = currentSetting.defaultGamePath
) : GameFileManger(versionNumber, here) {
	
	var kind: KindOfMod = KindOfMod.ORIGINAL
	
	var modDownloadInfo: ModDownloadInfoInterface? = null
		set(value) {
			kind = value.noNull().modKind
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
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Version) return false
		if (!super.equals(other)) return false
		
		if (kind != other.kind) return false
		if (modDownloadInfo != other.modDownloadInfo) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + kind.hashCode()
		result = 31 * result + (modDownloadInfo?.hashCode() ?: 0)
		return result
	}
	
	override fun toString(): String {
		return "Version(versionNumber=$versionNumber, kind=$kind, modDownloadInfo=$modDownloadInfo)"
	}
	
}

val preferredVersion: Version?
	get() = currentSetting.preferredVersion?.takeIf { it.ckeckIsEffective() }

class VersionsList(
	private val versions: HashSet<Version> = hashSetOf()
) : MutableSet<Version> by versions {
	fun Version.addToList(): Boolean = add(this)
	
	
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