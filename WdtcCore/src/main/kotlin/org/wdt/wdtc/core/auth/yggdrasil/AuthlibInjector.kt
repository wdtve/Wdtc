@file:JvmName("AuthlibInjector")

package org.wdt.wdtc.core.auth.yggdrasil

import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.utils.compareFile
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toFileData
import org.wdt.wdtc.core.utils.toURL

private const val BMCL_AUTHLIB_INJECTOR_LATEST_JSON =
	"https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json"
private const val AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/"

suspend fun downloadAuthlibInjector() {
	BMCL_AUTHLIB_INJECTOR_LATEST_JSON.toURL().toStrings().parseJsonObject().run {
		getString("download_url").toURL().let {
			if (authlibInjector.compareFile(it.toFileData())) {
				startDownloadTask(it, authlibInjector)
			}
		}
	}
}

