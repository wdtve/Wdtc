@file:JvmName("FileManger")

package org.wdt.wdtc.core.manger

import java.io.File

val minecraftComSkin: File = File(System.getProperty("user.home"), "AppData/Roaming/.minecraft/assets/skins")

val wdtcConfig: File = VMManger.wdtcConfigFromVM.resolve(".wdtc")

val wdtcSetting: File = wdtcConfig.resolve("setting")

val wdtcCache: File = wdtcConfig.resolve("cache")

val starterBat: File = wdtcCache.resolve("WdtcGameLauncherScript.bat")

val wdtcDependenciesDirectory: File = wdtcConfig.resolve("dependencies")

val wtdcOpenJFXDirectory: File = wdtcDependenciesDirectory.resolve("openjfx")

val wdtcUser: File = wdtcConfig.resolve("users")

val authlibInjector: File = wdtcDependenciesDirectory.resolve("authlib-injector.jar")

val userJson: File = wdtcUser.resolve("user.json")

val llbmpipeLoader: File = wdtcDependenciesDirectory.resolve("llvmpipe-loader.jar")

val userAsste: File = wdtcUser.resolve("assets")

val userListFile: File = wdtcUser.resolve("users.json")

val settingFile: File = wdtcSetting.resolve("setting.json")

val versionManifestFile: File = wdtcCache.resolve("versionManifest.json")

val tipsFile: File = wdtcConfig.resolve("assets").resolve("tips.txt")

val versionsJson: File = wdtcSetting.resolve("versions.json")
// TODO Plugin functionality
val pluginsDirectory: File = wdtcConfig.resolve("plugins")


