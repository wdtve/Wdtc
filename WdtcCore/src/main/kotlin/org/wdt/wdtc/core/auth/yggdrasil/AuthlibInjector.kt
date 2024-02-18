@file:JvmName("AuthlibInjector")

package org.wdt.wdtc.core.auth.yggdrasil

import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.utils.getJarManifestInfo
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL

private val BMCL_AUTHLIB_INJECTOR_LATEST_JSON =
  "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json".toURL()
private val AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/".toURL()

fun downloadauthlibInjector() {
  startDownloadTask(bmclAuthlibInjectorLatestJsonObject.getString("download_url"), authlibInjector)
}

fun updateAuthlibInjector() {
  if (!isOnline) return
  if (authlibInjector.isFileExists()) {
    val latestVersionNumber = bmclAuthlibInjectorLatestJsonObject.getString("version")
    val presentVersionNumber = authlibInjector.getJarManifestInfo("Implementation-Version")
    if (latestVersionNumber != presentVersionNumber) {
      downloadauthlibInjector()
    }
  } else {
    downloadauthlibInjector()
  }
}


private val bmclAuthlibInjectorLatestJsonObject = BMCL_AUTHLIB_INJECTOR_LATEST_JSON.toStrings().parseJsonObject()

