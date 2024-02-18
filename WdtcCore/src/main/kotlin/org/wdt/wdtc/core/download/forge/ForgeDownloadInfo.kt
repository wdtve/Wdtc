package org.wdt.wdtc.core.download.forge

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.gson.DownloadInfoTypeAdapter
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import org.wdt.wdtc.core.utils.unZipToFile
import java.io.File
import java.net.URL

// TODO Using 'Url' class
@JsonAdapter(DownloadInfoTypeAdapter::class)
open class ForgeDownloadInfo(protected val version: Version, override val modVersion: String) :
  ModDownloadInfoInterface {


  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) :
      this(version, versionsJsonObjectInterface.versionNumber)

  fun startDownloadInstallJar() {
    startDownloadTask(forgeInstallJarUrl, forgeInstallJarFile)
  }

  val forgeInstallJarFile: File
    get() = File(wdtcCache, "$modVersion-installer.jar")
  private val forgeInstallJarUrl: URL
    get() = "${downloadSource.forgeLibraryMavenUrl}net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar"
      .replace(":mcversion", version.versionNumber)
      .replace(":forgeversion", modVersion).toURL()

  fun unZipToInstallProfile() {
    unZipToFile(forgeInstallJarFile, forgeInstallProfileFile, "install_profile.json")
  }

  private val forgeInstallProfileFile: File
    get() = File(wdtcCache, "install_profile-${version.versionNumber}-$modVersion.json")

  val forgeInstallProfileJsonObject
    get() = forgeInstallProfileFile.readFileToJsonObject()
  val forgeVersionJsonFile: File
    get() = File(wdtcCache, "version-${version.versionNumber}-$modVersion.json")

  fun unZipToForgeVersionJson() {
    unZipToFile(forgeInstallJarFile, forgeVersionJsonFile, "version.json")
  }

  override fun toString(): String {
    return "ForgeDownloadInfo(modVersion='$modVersion')"
  }

  val forgeVersionJsonObject
    get() = forgeVersionJsonFile.readFileToJsonObject()

  val newForgeVersionJsonObject: ForgeVersionJsonObject
    get() = forgeVersionJsonFile.readFileToClass(forgeJsonObjectGsonBuilder)

  val newForgeInstallProfileJsonObject: ForgeInstallProfileJsonObject
    get() = forgeInstallProfileFile.readFileToClass()

  override val modInstallTask: ModInstallTaskInterface
    get() = ForgeInstallTask(version, modVersion)
  override val modKind: KindOfMod
    get() = KindOfMod.FORGE

}
