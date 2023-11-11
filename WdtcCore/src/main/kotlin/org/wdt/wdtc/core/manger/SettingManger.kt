package org.wdt.wdtc.core.manger

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.manger.DownloadSourceManger.DownloadSourceList
import org.wdt.wdtc.core.utils.JavaUtils
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.File

// TODO Remove SettingManger class
open class SettingManger {
	class Setting {
		var downloadSource = DownloadSourceList.OFFICIAL
		var console = false
		var llvmpipeLoader = false
		var defaultGamePath = File(System.getProperty("user.dir"))
		var javaPath: List<JavaUtils.JavaInfo> = ArrayList()
		var chineseLanguage = true
		var windowsWidth = 616.0
		var windowsHeight = 489.0
		var preferredVersion: String? = null
	}

	companion object {
		private val logmaker = getLogger(SettingManger::class.java)

		@JvmStatic
		val setting: Setting
			get() = FileManger.settingFile.readFileToClass()

		@JvmStatic
		fun putSettingToFile(setting: Setting) {
			FileManger.settingFile.writeObjectToFile(setting)
		}
	}
}