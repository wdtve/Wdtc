@file:JvmName("TaskManger")

package org.wdt.wdtc.core.manger

import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.*
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.getGameVersionList
import org.wdt.wdtc.core.utils.StopProcess
import org.wdt.wdtc.core.utils.startDownloadTask
import java.io.File
import java.util.*

fun ckeckVMConfig() {
  if (isDebug) {
    System.setProperty(CONFIG_PATH, "./")
  }
  if (System.getProperty(CONFIG_PATH) == null) {
    System.setProperty(CONFIG_PATH, System.getProperty("user.home"))
  }
}

fun CoroutineScope.runStartUpTask() {
  StopProcess.delete()
  File(wdtcConfig, "readme.txt").writeStringToFile(
    IOUtils.toString(
      this::class.java.getResourceAsStream("/assets/readme.txt") ?: throw RuntimeException()
    )
  )
  wdtcCache.createDirectories()
  if (userListFile.isFileNotExists()) {
    userListFile.writeObjectToFile(JsonObject())
  }
  if (settingFile.isFileNotExists()) {
    settingFile.writeObjectToFile(Setting())
  }
  if (llbmpipeLoader.isFileNotExists()) {
    launch(CoroutineName("Downlaod llbmpipeLoader")) {
      startDownloadTask(
        "https://maven.aliyun.com/repository/public/org/glavo/llvmpipe-loader/1.0/llvmpipe-loader-1.0.jar",
        llbmpipeLoader
      )
    }
  }
  if (versionManifestFile.isFileNotExists()) {
    launch(CoroutineName("Download version manifest json")) {
      DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
    }
  }
}

fun removeConfigDirectory(boolean: Boolean) {
  if (boolean) {
    wdtcConfig.deleteDirectory()
    val launchers =
      GameDirectoryManger(setting.defaultGamePath).getGameVersionList()
    if (!launchers.isNullOrEmpty()) {
      launchers.forEach {
        it.versionConfigFile.delete()
      }
    }
  }
}