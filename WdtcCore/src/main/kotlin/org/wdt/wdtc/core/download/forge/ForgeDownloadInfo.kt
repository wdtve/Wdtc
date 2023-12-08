package org.wdt.wdtc.core.download.forge

import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import org.wdt.wdtc.core.utils.ZipUtils.unZipToFile
import java.io.File

// TODO Using 'Url' class
open class ForgeDownloadInfo(protected val launcher: Launcher, override val modVersion: String) :
  ModDownloadInfoInterface {
  protected val source: DownloadSourceInterface = downloadSource
  private val logmaker = ForgeDownloadInfo::class.java.getWdtcLogger()


  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) :
      this(launcher, versionJsonObjectInterface.versionNumber!!)

  fun startDownloadInstallJar() {
    startDownloadTask(forgeInstallJarUrl, forgeInstallJarFile)
  }

  val installJarUrl: String
    get() = "${source.forgeLibraryMavenUrl}net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar"
  val forgeInstallJarFile: File
    get() = File(wdtcCache, "$modVersion-installer.jar")
  val forgeInstallJarUrl: String
    get() = installJarUrl.replace(":mcversion", launcher.versionNumber)
      .replace(":forgeversion", modVersion!!)

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
