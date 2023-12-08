package org.wdt.wdtc.core.manger

import com.google.gson.JsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.*
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.DownloadedGameVersion.getGameVersionList
import org.wdt.wdtc.core.manger.FileManger.llbmpipeLoader
import org.wdt.wdtc.core.manger.FileManger.settingFile
import org.wdt.wdtc.core.manger.FileManger.userListFile
import org.wdt.wdtc.core.manger.FileManger.versionManifestFile
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.utils.DownloadUtils
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.File
import java.io.IOException
import java.util.*

object TaskManger {
  private val logmaker = TaskManger::class.java.getWdtcLogger()

  @JvmStatic
  fun ckeckVMConfig() {
    if (VMManger.isDebug) {
      System.setProperty(VMManger.CONFIG_PATH, "./")
    }
    if (System.getProperty(VMManger.CONFIG_PATH) == null) {
      System.setProperty(VMManger.CONFIG_PATH, System.getProperty("user.home"))
    }
  }

  @JvmStatic
  @Throws(IOException::class)
  fun runStartUpTask() {
    DownloadUtils.StopProcess.delete()
    File(FileManger.wdtcConfig, "readme.txt").writeStringToFile(
      IOUtils.toString(
        SettingManger::class.java.getResourceAsStream("/assets/readme.txt") ?: throw RuntimeException()
      )
    )
    wdtcCache.createDirectories()
    if (userListFile.isFileNotExists()) {
      userListFile.writeObjectToFile(JsonObject())
    }
    if (settingFile.isFileNotExists()) {
      settingFile.writeObjectToFile(SettingManger.Setting())
    }
    if (llbmpipeLoader.isFileNotExists()) {
      startDownloadTask(
        "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar",
        llbmpipeLoader
      )
    }
    if (versionManifestFile.isFileNotExists()) {
      DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
    }
  }

  @JvmStatic
  fun removeConfigDirectory(boolean: Boolean) {
    if (boolean) {
      FileManger.wdtcConfig.deleteDirectory()
      val launchers =
        GameDirectoryManger(SettingManger.setting.defaultGamePath).getGameVersionList()
      if (!launchers.isNullOrEmpty()) {
        launchers.forEach {
          it.versionConfigFile.delete()
        }
      }
    }
  }
}
