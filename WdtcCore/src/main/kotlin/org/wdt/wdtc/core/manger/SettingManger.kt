@file:JvmName("SettingManger")

package org.wdt.wdtc.core.manger

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.utils.JavaUtils.JavaInfo
import java.io.File

data class Setting(
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


val setting: Setting = settingFile.readFileToClass()

fun putSettingToFile(setting: Setting) =
  settingFile.writeObjectToFile(setting, Json.getBuilder().setPrettyPrinting())