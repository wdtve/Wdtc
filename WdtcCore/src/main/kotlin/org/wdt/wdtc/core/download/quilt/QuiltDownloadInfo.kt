package org.wdt.wdtc.core.download.quilt

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.gson.DownloadInfoTypeAdapter
import java.io.File
import java.io.IOException

@JsonAdapter(DownloadInfoTypeAdapter::class)
open class QuiltDownloadInfo(
  protected val version: Version,
  override val modVersion: String
) : ModDownloadInfoInterface {
  private val libraryListUrl = "${downloadSource.quiltMetaUrl}/versions/loader/%s/%s/profile/json"

  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) :
      this(version, versionsJsonObjectInterface.versionNumber!!)

  val quiltVersionJson: File
    get() = File(wdtcCache, "${version.versionNumber}-quilt-$modVersion.json")
  val quiltVersionJsonUrl: String
    get() = libraryListUrl.format(version.versionNumber, modVersion)

  @get:Throws(IOException::class)
  val quiltGameVersionJsonObject
    get() = quiltVersionJson.readFileToJsonObject()
  override val modInstallTask: ModInstallTaskInterface
    get() = QuiltInstallTask(version, modVersion)
  override val modKind: KindOfMod
    get() = KindOfMod.QUILT

  override fun toString(): String {
    return "QuiltDownloadInfo(modVersion='$modVersion')"
  }

}
