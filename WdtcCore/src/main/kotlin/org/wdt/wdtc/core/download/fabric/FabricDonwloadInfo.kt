package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.officialDownloadSource
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.gson.DownloadInfoTypeAdapter
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.io.IOException
import java.net.URL

@JsonAdapter(DownloadInfoTypeAdapter::class)
open class FabricDonwloadInfo(
  protected val version: Version,
  override val modVersion: String
) : ModDownloadInfoInterface {
  var apiDownloadTask: FabricAPIDownloadTask? = null


  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
    version,
    versionsJsonObjectInterface.versionNumber.ckeckIsNull()
  )

  val fabricVersionFileUrl: String
    get() = "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/json"

  fun startDownloadProfileZip() {
    startDownloadTask(profileZipUrl, profileZipFile)
  }


  private val profileZipFile: File
    get() = File(
      wdtcCache,
      "${version.versionNumber}-$modVersion-frofile-zip.zip"
    )
  private val profileZipUrl: URL
    get() =
      "${officialDownloadSource.fabricMetaUrl}v2/versions/loader/%s/%s/profile/zip"
        .format(version.versionNumber, modVersion).toURL()


  private val fromFabricLoaderFolder: String
    get() = "fabric-loader-%s-%s".format(modVersion, version.versionNumber)


  val fabricJar: File
    get() = File(wdtcCache, "$fromFabricLoaderFolder/$fromFabricLoaderFolder.jar")
  val fabricVersionJson: File
    get() = File(wdtcCache, "${version.versionNumber}-fabric-$modVersion.json")

  @get:Throws(IOException::class)
  val fabricVersionJsonObject
    get() = fabricVersionJson.readFileToJsonObject()
  val isAPIDownloadTaskNoNull: Boolean
    get() = apiDownloadTask != null
  override val modInstallTask: ModInstallTaskInterface
    get() = FabricInstallTask(version, modVersion)
  override val modKind: KindOfMod
    get() = KindOfMod.FABRIC
  override fun toString(): String {
    return "FabricDonwloadInfo(modVersion='$modVersion', apiDownloadTask=$apiDownloadTask)"
  }


}
