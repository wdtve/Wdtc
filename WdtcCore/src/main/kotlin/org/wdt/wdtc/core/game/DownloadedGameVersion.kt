package org.wdt.wdtc.core.game

import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.utils.ModUtils.getModTask

object DownloadedGameVersion {
  @JvmStatic
  fun getGameVersionList(path: GameDirectoryManger): MutableList<Launcher>? {
    val gameVersionList: MutableList<Launcher> = ArrayList()
    val versionList = path.gameVersionsDirectory.listFiles()
    return if (versionList != null && versionList.isNotEmpty()) {
      for (versionFolder in versionList) {
        val launcher = Launcher(versionFolder.getName())
        if (launcher.versionJson.isFileExists()) {
          val child = getModTask(launcher)
          if (child != null) {
            gameVersionList.add(child)
          }
        }
      }
      gameVersionList
    } else {
      null
    }
  }

  @JvmStatic
  fun isDownloadedGame(path: GameDirectoryManger): Boolean {
    val list = getGameVersionList(path)
    return list?.isNotEmpty() ?: false
  }
}