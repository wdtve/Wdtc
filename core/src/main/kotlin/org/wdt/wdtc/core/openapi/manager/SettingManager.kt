@file:JvmName("SettingManager")

package org.wdt.wdtc.core.openapi.manager

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.JavaUtils
import org.wdt.wdtc.core.openapi.utils.gson.serializeVersionGson
import java.io.File

data class Setting(
	var downloadSource: DownloadSourceKind = DownloadSourceKind.OFFICIAL,
	var console: Boolean = false,
	var llvmpipeLoader: Boolean = false,
	var defaultGamePath: File = File(System.getProperty("user.dir")),
	var javas: Set<JavaUtils.JavaInfo> = setOf(),
	var chineseLanguage: Boolean = true,
	var windowsWidth: Double = 616.0,
	var windowsHeight: Double = 489.0,
	var preferredVersion: Version? = null
)


val currentSetting: Setting
	get() = settingFile.readFileToClass(serializeVersionGson)

fun Setting.putSettingToFile() {
	settingFile.writeObjectToFile(serializeVersionGson) {
		this
	}
}

inline fun Setting.saveSettingToFile(block: Setting.() -> Unit): Setting {
	return apply(block).also { it.putSettingToFile() }
}