package org.wdt.wdtc.core.auth.yggdrasil

import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.manger.FileManger.authlibInjector
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.IOException
import java.util.jar.JarInputStream

object AuthlibInjector {
	private const val BMCL_AUTHLIB_INJECTOR_LATEST_JSON =
		"https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json"
	private const val AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/"

	@Throws(IOException::class)
	fun downloadauthlibInjector() {
		startDownloadTask(bmclAuthlibInjectorLatestJsonObject.getString("download_url"), authlibInjector)
	}

	@JvmStatic
	@Throws(IOException::class)
	fun updateAuthlibInjector() {
		if (FileUtils.isFileExists(authlibInjector)) {
			val latestVersionNumber = bmclAuthlibInjectorLatestJsonObject.getString("version")
			val presentVersionNumber = JarInputStream(authlibInjector.newInputStream())
				.manifest.mainAttributes.getValue("Implementation-Version")
			if (latestVersionNumber != presentVersionNumber) {
				downloadauthlibInjector()
			}
		} else {
			downloadauthlibInjector()
		}
	}

	@get:Throws(IOException::class)
	private val bmclAuthlibInjectorLatestJsonObject
		get() = getURLToString(BMCL_AUTHLIB_INJECTOR_LATEST_JSON).parseJsonObject()
}
