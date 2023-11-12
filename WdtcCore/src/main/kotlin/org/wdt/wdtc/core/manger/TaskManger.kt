package org.wdt.wdtc.core.manger

import com.google.gson.JsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.*
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.DownloadedGameVersion
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
    FileUtils.delete(DownloadUtils.StopProcess)
    File(FileManger.wdtcConfig, "readme.txt").writeStringToFile(
      IOUtils.toString(Objects.requireNonNull(SettingManger::class.java.getResourceAsStream("/assets/readme.txt")))
    )
    FileManger.wdtcCache.createDirectories()
    if (FileManger.userListFile.isFileNotExists()) {
      FileManger.userListFile.writeObjectToFile(JsonObject())
    }
    if (FileManger.settingFile.isFileNotExists()) {
      FileManger.settingFile.writeObjectToFile(SettingManger.Setting())
    }
    val llbmpipeLoader =
      "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar"
    if (FileManger.llbmpipeLoader.isFileNotExists()) {
      startDownloadTask(llbmpipeLoader, FileManger.llbmpipeLoader)
    }
    if (FileManger.versionManifestFile.isFileNotExists()) {
      DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
    }
  }

  @JvmStatic
  fun removeConfigDirectory(boolean: Boolean) {
    if (boolean) {
      FileManger.wdtcConfig.deleteDirectory()
      val launchers =
        DownloadedGameVersion.getGameVersionList(GameDirectoryManger(SettingManger.setting.defaultGamePath))
      if (!launchers.isNullOrEmpty()) {
        for (launcher in launchers) {
          launcher.versionConfigFile.delete()
        }

      }
    }
  }
}
