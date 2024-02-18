package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.game.GameVersionsList
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.runIfNoNull
import java.io.File


open class GameDirectoryManger {
  //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
  val workDirectory: File

  constructor() {
    workDirectory = currentSetting.defaultGamePath
  }

  constructor(here: File) {
    this.workDirectory = here
  }

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

val GameDirectoryManger.gameVersionList: GameVersionsList
  get() = GameVersionsList().apply {
    this@gameVersionList.gameVersionsDirectory.listFiles().run {
      runIfNoNull {
        if (isNotEmpty()) {
          forEach {
            Version(it.name).addToList()
          }
        }
      }
    }
  }


val GameDirectoryManger.isDownloadedGame: Boolean
  get() = this.gameVersionList.isNotEmpty()

