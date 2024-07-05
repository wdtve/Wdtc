package org.wdt.wdtc.core.openapi.manager

import org.wdt.wdtc.core.openapi.game.VersionImpl
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.utils.noNull
import java.io.File


interface GameDirectoryManager {
	val workDirectory: File
	val gameAssetsObjectsDirectory: File
	val gameDirectory: File
	val gameLibraryDirectory: File
	val gameVersionsDirectory: File
	
	val gameAssetsDirectory: File
	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
	
	companion object {
		val DEFAULT_GAME_DIRECTORY = GameDirectoryManagerImpl()
	}
}

class GameDirectoryManagerImpl(
	override val workDirectory: File = currentSetting.defaultGamePath
) : GameDirectoryManager {
	override val gameAssetsObjectsDirectory: File
		get() = File(gameAssetsDirectory, "objects")
	override val gameDirectory: File
		get() = File(workDirectory, ".minecraft")
	override val gameLibraryDirectory: File
		get() = File(gameDirectory, "libraries")
	override val gameVersionsDirectory: File
		get() = File(gameDirectory, "versions")
	override val gameAssetsDirectory: File
		get() = File(gameDirectory, "assets")
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is GameDirectoryManager) return false
		
		if (workDirectory != other.workDirectory) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		return workDirectory.hashCode()
	}
	
}

val defaultHere: File
	get() = File(System.getProperty("user.dir"))

val GameDirectoryManager.currentDownlaodedVersionList: VersionsList
	get() = gameVersionsDirectory.listFiles()?.map {
		VersionImpl(it.name).setModTask()
	}?.filter {
		it != null && it.versionJson.exists()
	}?.mapTo(VersionsList()) { it.noNull() } ?: VersionsList()


val GameDirectoryManager.isDownloadedGame: Boolean
	get() = this.currentDownlaodedVersionList.isNotEmpty()

