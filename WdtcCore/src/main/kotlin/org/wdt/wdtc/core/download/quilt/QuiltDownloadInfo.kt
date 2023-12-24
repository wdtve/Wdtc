package org.wdt.wdtc.core.download.quilt

import com.google.gson.annotations.JsonAdapter
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.manger.wdtcCache
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.gson.DownloadInfoTypeAdapter
import java.io.File
import java.io.IOException

@JsonAdapter(DownloadInfoTypeAdapter::class)
open class QuiltDownloadInfo(
  protected val launcher: Launcher,
  override val modVersion: String
) : ModDownloadInfoInterface {
  private val libraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s/profile/json"

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) :
      this(launcher, versionJsonObjectInterface.versionNumber!!)

  val quiltVersionJson: File
    get() = File(wdtcCache, "${launcher.versionNumber}-quilt-$modVersion.json")
  val quiltVersionJsonUrl: String
    get() = libraryListUrl.format(launcher.versionNumber, modVersion)

  @get:Throws(IOException::class)
  val quiltGameVersionJsonObject
    get() = quiltVersionJson.readFileToJsonObject()
  override val modInstallTask: InstallTaskInterface
    get() = QuiltInstallTask(launcher, modVersion)
  override val modKind: KindOfMod
    get() = KindOfMod.QUILT

  override fun toString(): String {
    return "QuiltDownloadInfo(modVersion='$modVersion')"
  }

}
