package org.wdt.wdtc.core.manger

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.manger.DownloadSourceManger.DownloadSourceList
import org.wdt.wdtc.core.utils.JavaUtils
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.File

// TODO Remove SettingManger class
open class SettingManger {
  class Setting {
    var downloadSource = DownloadSourceList.OFFICIAL
    var console = false
    var llvmpipeLoader = false
    var defaultGamePath = File(System.getProperty("user.dir"))
    var javaPath: MutableList<JavaUtils.JavaInfo> = ArrayList()
    var chineseLanguage = true
    var windowsWidth = 616.0
    var windowsHeight = 489.0
    var preferredVersion: String? = null
    override fun toString(): String {
      return "Setting(downloadSource=$downloadSource, console=$console, llvmpipeLoader=$llvmpipeLoader, defaultGamePath=$defaultGamePath, javaPath=$javaPath, chineseLanguage=$chineseLanguage, windowsWidth=$windowsWidth, windowsHeight=$windowsHeight, preferredVersion=$preferredVersion)"
    }

  }

  companion object {
    private val logmaker = SettingManger::class.java.getWdtcLogger()

    @JvmStatic
    val setting: Setting
      get() = FileManger.settingFile.readFileToClass()

    @JvmStatic
    fun putSettingToFile(setting: Setting) {
      FileManger.settingFile.writeStringToFile(Json.GSON_BUILDER.setPrettyPrinting().create().toJson(setting))
    }
  }
}