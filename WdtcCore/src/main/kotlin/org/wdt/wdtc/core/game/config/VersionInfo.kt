package org.wdt.wdtc.core.game.config

import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.modDownloadInfo

data class VersionInfo(
  val versionNumber: String,

  val kind: KindOfMod,

  var modVersion: String? = null
) {

  constructor(version: Version) : this(
    version.versionNumber,
    version.kind,
    version.modDownloadInfo?.modVersion
  )

  val version: Version
    get() {
      val modVersion = this.modVersion ?: throw NullPointerException("$modVersion is null")
      val version = Version(versionNumber).apply {
        when (kind) {
          KindOfMod.FORGE -> forgeModDownloadInfo = ForgeDownloadInfo(this, modVersion)
          KindOfMod.FABRIC -> fabricModInstallInfo = FabricDonwloadInfo(this, modVersion)
          KindOfMod.QUILT -> quiltModDownloadInfo = QuiltInstallTask(this, modVersion)
          else -> {
            throw VersionNotFoundException("Game mod not found")
          }
        }
      }
      return version
    }
}

val Version.versionInfo
  get() = VersionInfo(this)
