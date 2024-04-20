package org.wdt.wdtc.core.openapi.manger

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.GameVersionJsonObject
import org.wdt.wdtc.core.openapi.game.serializeGameVersionJsonObjectGson
import java.io.File
import java.io.IOException
import java.util.*


open class GameFileManger(
	val versionNumber: String,
	here: File = currentSetting.defaultGamePath
) : GameDirectoryManger(here) {
	
	val versionDirectory: File
		get() = File(gameVersionsDirectory, versionNumber)
	val versionJson: File
		get() = File(versionDirectory, "$versionNumber.json")
	val versionJar: File
		get() = File(versionDirectory, "$versionNumber.jar")
	val versionLog4j2: File
		get() = File(versionDirectory, "log4j2.xml")
	val versionNativesPath: File
		get() = File(versionDirectory, "natives-windows-x86_64")
	
	@get:Throws(IOException::class)
	val gameAssetsListJson: File
		get() = File(gameAssetsDirectory, "indexes/${gameVersionJsonObject.assets}.json")
	val gameOptionsFile: File
		get() = File(versionDirectory, "options.txt")
	val gameModsPath: File
		get() = File(versionDirectory, "mods")
	val gameLogDirectory: File
		get() = File(versionDirectory, "logs")
	
	fun GameVersionJsonObject.putToVersionJson() {
		versionJson.writeObjectToFile(this, serializeGameVersionJsonObjectGson)
	}
	
	
	val gameVersionJsonObject: GameVersionJsonObject
		get() = versionJson.run {
			readFileToClass(serializeGameVersionJsonObjectGson)
		}
	
	val laucnherProfiles: File
		get() = File(gameDirectory, "Launcher_profiles.json")
	val versionConfigFile: File
		get() = File(versionDirectory, "config.json")
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is GameFileManger) return false
		if (!super.equals(other)) return false
		
		if (versionNumber != other.versionNumber) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + versionNumber.hashCode()
		return result
	}
}