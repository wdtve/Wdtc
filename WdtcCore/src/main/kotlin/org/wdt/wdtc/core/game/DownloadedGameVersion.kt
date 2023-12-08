package org.wdt.wdtc.core.game

import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.utils.ModUtils.setModTask

object DownloadedGameVersion {
  fun GameDirectoryManger.getGameVersionList(): MutableList<Launcher>? {
    val gameVersionList: MutableList<Launcher> = ArrayList()
    val versionList = this.gameVersionsDirectory.listFiles()
    return if (versionList != null && versionList.isNotEmpty()) {
      for (versionFolder in versionList) {
        val launcher = Launcher(versionFolder.getName())
        if (launcher.versionJson.isFileExists()) {
          val child = launcher.setModTask()
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
  val GameDirectoryManger.isDownloadedGame: Boolean
    get() = this.getGameVersionList()?.isNotEmpty() ?: false
}