package org.wdt.wdtc.core.manger

import java.io.File


open class GameDirectoryManger {
    //此必须在编辑运行配置里把工作目录改成"$ProjectFileDir$"不然游戏文件夹地址会出错
    var here: File

    constructor() {
        here = SettingManger.setting.defaultGamePath
    }

    constructor(here: File) {
        this.here = here
    }

    val gameObjects: File
        get() = File(gameAssetsDirectory, "objects")
    val gameDirectory: File
        get() = File(here, ".minecraft")
    val gameLibraryDirectory: File
        get() = File(gameDirectory, "libraries")
    val gameVersionsDirectory: File
        get() = File(gameDirectory, "versions")
    val gameAssetsDirectory: File
        get() = File(gameDirectory, "assets")

    companion object {
        @JvmStatic
        val defaultHere: File
            get() = File(System.getProperty("user.dir"))
    }
}
