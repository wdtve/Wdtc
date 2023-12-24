package org.wdt.wdtc.core.game.config

import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.modDownloadInfo

data class VersionInfo(
  val versionNumber: String,

  val kind: KindOfMod,

  var modVersion: String? = null
) {

  constructor(launcher: Launcher) : this(
    launcher.versionNumber,
    launcher.kind,
    launcher.modDownloadInfo?.modVersion
  )

  val launcher: Launcher
    get() {
      val launcher = Launcher(versionNumber)
      val modVersion = this.modVersion ?: throw NullPointerException("$modVersion is null")
      when (kind) {
        KindOfMod.FORGE -> launcher.forgeModDownloadInfo = ForgeDownloadInfo(launcher, modVersion)
        KindOfMod.FABRIC -> launcher.fabricModInstallInfo = FabricDonwloadInfo(launcher, modVersion)
        KindOfMod.QUILT -> launcher.quiltModDownloadInfo = QuiltInstallTask(launcher, modVersion)
        else -> {
          throw VersionNotFoundException("Game mod not found")
        }
      }
      return launcher
    }
}

val Launcher.versionInfo
  get() = VersionInfo(this)
