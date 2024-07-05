package org.wdt.wdtc.core.openapi.manager

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.GameVersionJsonObject
import org.wdt.wdtc.core.openapi.game.serializeGameVersionJsonObjectGson
import java.io.File


interface GameFileManager : GameDirectoryManager {
	val versionNumber: String
	
	val versionDirectory: File
	
	val versionJson: File
	
	val versionJar: File
	
	val versionLog4j2: File
	
	val versionNativesPath: File
	
	val gameAssetsListJson: File
	
	val gameOptionsFile: File
	
	val gameModsPath: File
	
	val gameLogDirectory: File
	
	val versionConfigFile: File
	
	
	val laucnherProfiles: File
	
	
	override fun equals(other: Any?): Boolean
	
	override fun hashCode(): Int
	
	fun GameVersionJsonObject.putToVersionJson() {
		versionJson.writeObjectToFile(serializeGameVersionJsonObjectGson) { this }
	}
	
	val gameVersionJsonObject: GameVersionJsonObject
		get() = versionJson.readFileToClass(serializeGameVersionJsonObjectGson)
}