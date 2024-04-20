package org.wdt.wdtc.core.openapi.manger

import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.utils.runIfNoNull
import java.io.File


open class GameDirectoryManger(
	//此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
	val workDirectory: File
) {
	
	val gameAssetsObjectsDirectory: File
		get() = File(gameAssetsDirectory, "objects")
	val gameDirectory: File
		get() = File(workDirectory, ".minecraft")
	val gameLibraryDirectory: File
		get() = File(gameDirectory, "libraries")
	val gameVersionsDirectory: File
		get() = File(gameDirectory, "versions")
	val gameAssetsDirectory: File
		get() = File(gameDirectory, "assets")
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is GameDirectoryManger) return false
		
		if (workDirectory != other.workDirectory) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		return workDirectory.hashCode()
	}
	
	companion object {
		val DEFAULT_GAME_DIRECTORY = GameDirectoryManger(currentSetting.defaultGamePath)
	}
}

val defaultHere: File
	get() = File(System.getProperty("user.dir"))

val GameDirectoryManger.currentDownlaodedVersionList: VersionsList
	get() = VersionsList().apply {
		gameVersionsDirectory.listFiles().run {
			runIfNoNull {
				forEach {
					Version(it.name).setModTask().let { version ->
						if (version != null && version.versionJson.isFileExists()) {
							version.addToList()
						}
					}
				}
			}
		}
	}


val GameDirectoryManger.isDownloadedGame: Boolean
	get() = this.currentDownlaodedVersionList.isNotEmpty()

