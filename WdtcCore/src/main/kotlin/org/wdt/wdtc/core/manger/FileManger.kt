@file:JvmName("FileManger")

package org.wdt.wdtc.core.manger

import java.io.File

val minecraftComSkin: File
  get() = File(System.getProperty("user.home"), "AppData/Roaming/.minecraft/assets/skins")

val wdtcConfig: File
  get() = File(wdtcConfigFromVM, ".wdtc")

val starterBat: File
  get() = File(wdtcCache, "WdtcGameLauncherScript.bat")

val authlibInjector: File
  get() = File(wdtcImplementationPath, "authlib-injector.jar")

val userJson: File
  get() = File(wdtcUser, "user.json")

val llbmpipeLoader: File
  get() = File(wdtcImplementationPath, "llvmpipe-loader.jar")

val wdtcCache: File
  get() = File(wdtcConfig, "cache")

val wdtcImplementationPath: File
  get() = File(wdtcConfig, "dependencies")

val wtdcOpenJFXPath: File
  get() = File(wdtcImplementationPath, "openjfx")
val wdtcUser: File
  get() = File(wdtcConfig, "users")
val userAsste: File
  get() = File(wdtcUser, "assets")

val userListFile: File
  get() = File(wdtcUser, "users.json")

val settingFile: File
  get() = File(wdtcConfig, "setting/setting.json")

val versionManifestFile: File
  get() = File(wdtcCache, "versionManifest.json")

