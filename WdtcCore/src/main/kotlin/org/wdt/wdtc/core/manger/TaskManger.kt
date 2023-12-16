@file:JvmName("TaskManger")

package org.wdt.wdtc.core.manger

import com.google.gson.JsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.*
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.getGameVersionList
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.stopProcess
import org.wdt.wdtc.core.utils.toURL
import java.io.File

fun ckeckVMConfig() {
  if (isDebug) System.setProperty(CONFIG_PATH, "./")
  if (System.getProperty(CONFIG_PATH) == null) {
    System.setProperty(CONFIG_PATH, System.getProperty("user.home"))
  }
}

fun runStartUpTask() {
  stopProcess.delete()
  File(wdtcConfig, "readme.txt").writeStringToFile(
    object {}.javaClass.getResourceAsStream("/assets/readme.txt")?.toStrings()
      ?: throw NullPointerException("readme.txt is null")
  )
  wdtcCache.createDirectories()
  if (userListFile.isFileNotExists()) {
    userListFile.writeObjectToFile(JsonObject())
  }
  if (settingFile.isFileNotExists()) {
    settingFile.writeObjectToFile(Setting())
  }
  if (llbmpipeLoader.isFileNotExists()) {
    startDownloadTask(
      "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar".toURL(),
      llbmpipeLoader
    )
  }
  if (versionManifestFile.isFileNotExists()) {
    DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
  }

}

fun removeConfigDirectory(boolean: Boolean) {
  if (boolean) {
    wdtcConfig.deleteDirectory()
    val launchers = GameDirectoryManger(setting.defaultGamePath).getGameVersionList()
    if (!launchers.isNullOrEmpty()) {
      launchers.forEach {
        it.versionConfigFile.delete()
      }
    }
  }
}