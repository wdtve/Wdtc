package org.wdt.wdtc.core.download.forge

import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import org.wdt.wdtc.core.utils.unZipToFile
import java.io.File
import java.net.URL

// TODO Using 'Url' class
open class ForgeDownloadInfo(protected val launcher: Launcher, override val modVersion: String) :
  ModDownloadInfoInterface {


  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) :
      this(launcher, versionJsonObjectInterface.versionNumber!!)

  fun startDownloadInstallJar() {
    startDownloadTask(forgeInstallJarUrl, forgeInstallJarFile)
  }

  private val installJarUrl: String
    get() = "${downloadSource.forgeLibraryMavenUrl}net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar"
  val forgeInstallJarFile: File
    get() = File(wdtcCache, "$modVersion-installer.jar")
  private val forgeInstallJarUrl: URL
    get() = installJarUrl.replace(":mcversion", launcher.versionNumber)
      .replace(":forgeversion", modVersion).toURL()

  fun unZipToInstallProfile() {
    unZipToFile(forgeInstallJarFile, installProfileFile, "install_profile.json")
  }

  val installProfileFile: File
    get() = File(wdtcCache, "install_profile-${launcher.versionJson}-$modVersion.json")

  val installPrefileJSONObject
    get() = installProfileFile.readFileToJsonObject()
  val forgeVersionJsonFile: File
    get() = File(wdtcCache, "version-${launcher.versionNumber}-$modVersion.json")

  fun unZipToForgeVersionJson() {
    unZipToFile(forgeInstallJarFile, forgeVersionJsonFile, "version.json")
  }

  val forgeVersionJsonObject
    get() = forgeVersionJsonFile.readFileToJsonObject()
  override val modInstallTask: InstallTaskInterface
    get() = ForgeInstallTask(launcher, modVersion)
  override val modKind: KindOfMod
    get() = KindOfMod.FORGE
}
