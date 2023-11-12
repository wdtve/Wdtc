package org.wdt.wdtc.core.manger

import java.io.File

object FileManger {
  @JvmStatic
  val minecraftComSkin: File
    get() = File(System.getProperty("user.home"), "AppData/Roaming/.minecraft/assets/skins")

  @JvmStatic
  val wdtcConfig: File
    get() = File(VMManger.wdtcConfigFromVM, ".wdtc")

  @JvmStatic
  val starterBat: File
    get() = File(wdtcCache, "WdtcGameLauncherScript.bat")

  @JvmStatic
  val authlibInjector: File
    get() = File(wdtcImplementationPath, "authlib-injector.jar")

  @JvmStatic
  val userJson: File
    get() = File(wdtcUser, "user.json")

  @JvmStatic
  val llbmpipeLoader: File
    get() = File(wdtcImplementationPath, "llvmpipe-loader.jar")

  @JvmStatic
  val wdtcCache: File
    get() = File(wdtcConfig, "cache")

  @JvmStatic
  val wdtcImplementationPath: File
    get() = File(wdtcConfig, "dependencies")

  @JvmStatic
  val wtdcOpenJFXPath: File
    get() = File(wdtcImplementationPath, "openjfx")
  val wdtcUser: File
    get() = File(wdtcConfig, "users")
  val userAsste: File
    get() = File(wdtcUser, "assets")

  @JvmStatic
  val userListFile: File
    get() = File(wdtcUser, "users.json")

  @JvmStatic
  val settingFile: File
    get() = File(wdtcConfig, "setting/setting.json")

  @JvmStatic
  val versionManifestFile: File
    get() = File(wdtcCache, "versionManifest.json")
}
