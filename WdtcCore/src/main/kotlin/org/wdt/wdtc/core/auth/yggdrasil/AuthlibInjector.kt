@file:JvmName("AuthlibInjector")

package org.wdt.wdtc.core.auth.yggdrasil

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseJsonObject
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.newInputStream
import org.wdt.wdtc.core.manger.authlibInjector
import org.wdt.wdtc.core.utils.getURLToString
import org.wdt.wdtc.core.utils.startDownloadTask
import java.util.jar.JarInputStream

private const val BMCL_AUTHLIB_INJECTOR_LATEST_JSON =
  "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/artifact/latest.json"
private const val AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/"

fun downloadauthlibInjector() {
  startDownloadTask(bmclAuthlibInjectorLatestJsonObject.getString("download_url"), authlibInjector)
}

fun CoroutineScope.updateAuthlibInjector() {
  launch(CoroutineName("Check authlibInjector version")) {
    if (authlibInjector.isFileExists()) {
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
}

private val bmclAuthlibInjectorLatestJsonObject = BMCL_AUTHLIB_INJECTOR_LATEST_JSON.getURLToString().parseJsonObject()

