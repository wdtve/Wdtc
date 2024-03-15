@file:JvmName("FileManger")

package org.wdt.wdtc.core.manger

import java.io.File

val minecraftComSkin: File = File(System.getProperty("user.home"), "AppData/Roaming/.minecraft/assets/skins")

val wdtcConfig: File = File(VMManger.wdtcConfigFromVM, ".wdtc")

val wdtcSetting: File = File(wdtcConfig, "setting")

val wdtcCache: File = File(wdtcConfig, "cache")

val starterBat: File = File(wdtcCache, "WdtcGameLauncherScript.bat")

val wdtcDependenciesDirectory: File = File(wdtcConfig, "dependencies")

val wtdcOpenJFXDirectory: File = File(wdtcDependenciesDirectory, "openjfx")

val wdtcUser: File = File(wdtcConfig, "users")

val authlibInjector: File = File(wdtcDependenciesDirectory, "authlib-injector.jar")

val userJson: File = File(wdtcUser, "user.json")

val llbmpipeLoader: File = File(wdtcDependenciesDirectory, "llvmpipe-loader.jar")

val userAsste: File = File(wdtcUser, "assets")

val userListFile: File = File(wdtcUser, "users.json")

val settingFile: File = File(wdtcSetting, "setting.json")

val versionManifestFile: File = File(wdtcCache, "versionManifest.json")

val tipsFile: File = File(wdtcConfig, "assets/tips.txt")

val versionsJson: File = File(wdtcSetting, "versions.json")


