package org.wdt.wdtc.core.manger

import java.io.File
import java.lang.Boolean.getBoolean

object VMManger {
  const val LAUNCHER_VERSION = "wdtc.launcher.version"
  const val CONFIG_PATH = "wdtc.config.path"
  const val DEBUG = "wdtc.debug.switch"
  const val APPLICATION_TYPE = "wtdc.application.type"
  const val CLIENT_ID = "wtdc.oauth.clientId"

  // TODO More and more people
  private val LAUNCHER_AUTHOR = listOf("Wdt~")
  private val OS = System.getProperty("os.name")
  val clientId: String
    get() = System.getProperty(CLIENT_ID, "8c4a5ce9-55b9-442e-9bd0-17cf89689dd0")

  @JvmStatic
  val launcherVersion: String
    get() = System.getProperty(LAUNCHER_VERSION, "demo")

  @JvmStatic
  val isDebug: Boolean
    get() = getBoolean(DEBUG)

  @JvmStatic
  val wdtcConfigFromVM: File
    get() {
      val wdtcConfigPath = System.getProperty(CONFIG_PATH)
      return if (wdtcConfigPath != null) File(File(wdtcConfigPath).canonicalPath)
      else File(System.getProperty("user.home"))
    }

  @JvmStatic
  val applicationType: String
    get() = System.getProperty(APPLICATION_TYPE, "ui")
  val isConsole: Boolean
    get() = applicationType == "console"
  val isUI: Boolean
    get() = applicationType == "ui"
}
