package org.wdt.wdtc.core.manger

import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.game.VersionsList
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.noNull
import org.wdt.wdtc.core.utils.runIfNoNull
import org.wdt.wdtc.core.utils.setModTask
import java.io.File


open class GameDirectoryManger(
	//此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
	val workDirectory: File = currentSetting.defaultGamePath
) {
	
	val gameObjects: File
		get() = File(gameAssetsDirectory, "objects")
	val gameDirectory: File
		get() = File(workDirectory, ".minecraft")
	val gameLibraryDirectory: File
		get() = File(gameDirectory, "libraries")
	val gameVersionsDirectory: File
		get() = File(gameDirectory, "versions")
	val gameAssetsDirectory: File
		get() = File(gameDirectory, "assets")
}

val defaultHere: File
	get() = File(System.getProperty("user.dir"))

val GameDirectoryManger.gameVersionList: VersionsList
	get() = VersionsList().apply {
		this@gameVersionList.gameVersionsDirectory.listFiles().run {
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
	get() = this.gameVersionList.isNotEmpty()

