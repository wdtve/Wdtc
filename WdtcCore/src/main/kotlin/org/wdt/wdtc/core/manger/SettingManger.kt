package org.wdt.wdtc.core.manger

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.manger.DownloadSourceManger.DownloadSourceList
import org.wdt.wdtc.core.manger.FileManger.settingFile
import org.wdt.wdtc.core.utils.JavaUtils.JavaInfo
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.File

// TODO Remove SettingManger class
open class SettingManger {
  data class Setting @JvmOverloads constructor(
    var downloadSource: DownloadSourceList = DownloadSourceList.OFFICIAL,
    var console: Boolean = false,
    var llvmpipeLoader: Boolean = false,
    var defaultGamePath: File = File(System.getProperty("user.dir")),
    var javaPath: MutableSet<JavaInfo> = HashSet(),
    var chineseLanguage: Boolean = true,
    var windowsWidth: Double = 616.0,
    var windowsHeight: Double = 489.0,
    var preferredVersion: String? = null
  )

  companion object {
    private val logmaker = SettingManger::class.java.getWdtcLogger()

    @JvmStatic
    val setting: Setting = settingFile.readFileToClass()

    @JvmStatic
    fun putSettingToFile(setting: Setting) =
      settingFile.writeObjectToFile(setting, Json.getBuilder().setPrettyPrinting())

  }
}