@file:JvmName("AuthlibInjector")

package org.wdt.wdtc.core.impl.auth.accounts.yggdrasil

import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.openapi.manger.authlibInjector
import org.wdt.wdtc.core.openapi.utils.compareFile
import org.wdt.wdtc.core.openapi.utils.startDownloadTask
import org.wdt.wdtc.core.openapi.utils.toFileData
import org.wdt.wdtc.core.openapi.utils.toURL

private const val BMCL_AUTHLIB_INJECTOR_LATEST_JSON =
	"https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json"
private const val AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/"

suspend fun downloadAuthlibInjector() {
	BMCL_AUTHLIB_INJECTOR_LATEST_JSON.toURL().toStrings().parseJsonObject().getString("download_url").toURL().let {
		if (authlibInjector compareFile it.toFileData()) {
				startDownloadTask(it, authlibInjector)
			}
		}
	
}

