@file:JvmName("WdtcMain")

package org.wdt.wdtc.ui

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.auth.printUserList
import org.wdt.wdtc.core.auth.yggdrasil.updateAuthlibInjector
import org.wdt.wdtc.core.game.config.writeConfigJsonToAllVersion
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.JavaUtils
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.ui.window.setErrorWin
import java.util.*
import kotlin.concurrent.thread

fun main(args: Array<String>) {
  try {
    ckeckVMConfig()
    if (args.isNotEmpty()) {
      removeConfigDirectory(Arrays.stream(args).toList()[0] == "refresh")
    }
    ckeckJavaFX()
    logmaker.info("===== Wdtc - $launcherVersion - =====")
    logmaker.info("Java Version:${System.getProperty("java.version")}")
    logmaker.info("Java VM Version:${System.getProperty("java.vm.name")}")
    logmaker.info("Java Home:${System.getProperty("java.home")}")
    logmaker.info("Wdtc Debug Mode:$isDebug")
    logmaker.info("Wdtc Application Type:$applicationType")
    logmaker.info("Wdtc Config Path:$wdtcConfig")
    logmaker.info("Setting File:$settingFile")
    logmaker.info("Here:$defaultHere")
    runStartUpTask()
    removePreferredVersion()
    printUserList()
    if (isOnline) {
      downloadVersionManifestJsonFileTask()
      updateAuthlibInjector()
    }
    writeConfigJsonToAllVersion()
    thread(name = "Found Java") { JavaUtils.main(registryKey) }
    javafx.application.Application.launch(AppMain::class.java, *args)
  } catch (e: Throwable) {
    setErrorWin(e)
  }
}

private fun removePreferredVersion() {
  val setting = setting
  val version = setting.preferredVersion
  if (version != null) {
    if (version.versionJson.isFileNotExists()) {
      setting.preferredVersion = null
      setting.putSettingToFile()
    }
  }
}

private val registryKey: Array<String> = arrayOf(
  "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit",
  "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Update",
  "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Web Start Caps",
  "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK"
)

