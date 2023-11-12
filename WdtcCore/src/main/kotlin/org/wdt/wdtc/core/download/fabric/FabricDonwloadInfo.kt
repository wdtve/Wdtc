package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.DownloadSourceManger.officialDownloadSource
import org.wdt.wdtc.core.manger.FileManger.wdtcCache
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.File
import java.io.IOException

open class FabricDonwloadInfo(protected val launcher: Launcher, override val modVersion: String?) :
  ModDownloadInfoInterface {
  var apiDownloadTask: FabricAPIDownloadTask? = null
  private val logmaker = FabricDonwloadInfo::class.java.getWdtcLogger()


  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
    launcher,
    versionJsonObjectInterface.versionNumber
  )

  val fabricVersionFileUrl: String
    get() = "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/json"

  fun startDownloadProfileZip() {
    startDownloadTask(profileZipUrl, profileZipFile)
  }

  val profileZipFile: File
    get() = File(
      wdtcCache,
      "${launcher.versionNumber}-$modVersion-frofile-zip.zip"
    )
  val profileZipUrl: String
    get() =
      "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/zip".format(
        launcher.versionNumber,
        modVersion
      )


  val fromFabricLoaderFolder: String
    get() = "fabric-loader-%s-%s".format(modVersion, launcher.versionNumber)


  val fabricJar: File
    get() = File(wdtcCache, "$fromFabricLoaderFolder/$fromFabricLoaderFolder.jar")
  val fabricVersionJson: File
    get() = File(wdtcCache, "${launcher.versionNumber}-fabric-$modVersion.json")

  @get:Throws(IOException::class)
  val fabricVersionJsonObject
    get() = fabricVersionJson.readFileToJsonObject()
  val isAPIDownloadTaskNoNull: Boolean
    get() = apiDownloadTask != null
  override val modInstallTask: InstallTaskInterface
    get() = FabricInstallTask(launcher, modVersion)
  override val modKind: KindOfMod?
    get() = KindOfMod.FABRIC

}
