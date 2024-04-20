@file:JvmName("SettingManger")

package org.wdt.wdtc.core.openapi.manger

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.JavaUtils
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionGson
import org.wdt.wdtc.core.openapi.utils.launchScope
import java.io.File

data class Setting(
	var downloadSource: DownloadSourceKind = DownloadSourceKind.OFFICIAL,
	var console: Boolean = false,
	var llvmpipeLoader: Boolean = false,
	var defaultGamePath: File = File(System.getProperty("user.dir")),
	var javaPath: MutableSet<JavaUtils.JavaInfo> = HashSet(),
	var chineseLanguage: Boolean = true,
	var windowsWidth: Double = 616.0,
	var windowsHeight: Double = 489.0,
	var preferredVersion: Version? = null
)


val currentSetting: Setting
	get() = settingFile.readFileToClass(serializeVersionGson)

fun Setting.putSettingToFile() {
	launchScope {
		settingFile.writeObjectToFile(this@putSettingToFile, serializeVersionGson)
	}
}

inline fun Setting.changeSettingToFile(block: Setting.() -> Unit): Setting {
	return apply(block).also { it.putSettingToFile() }
}