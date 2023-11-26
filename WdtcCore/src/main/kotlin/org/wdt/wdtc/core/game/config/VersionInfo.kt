package org.wdt.wdtc.core.game.config

import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.ModUtils.getModDownloadInfo

data class VersionInfo @JvmOverloads constructor(
  val versionNumber: String,

  val kind: KindOfMod,

  var modVersion: String? = null
) {

  constructor(launcher: Launcher) : this(
    launcher.versionNumber,
    launcher.kind,
    getModDownloadInfo(launcher)?.modVersion
  )

  val launcher: Launcher
    get() {
      val launcher = Launcher(versionNumber)
      when (kind) {
        KindOfMod.FORGE -> launcher.setForgeModDownloadInfo(ForgeDownloadInfo(launcher, modVersion))
        KindOfMod.FABRIC -> launcher.setFabricModInstallInfo(FabricDonwloadInfo(launcher, modVersion))
        KindOfMod.QUILT -> launcher.setQuiltModDownloadInfo(QuiltInstallTask(launcher, modVersion))
        else -> {
          throw VersionNotFoundException("Game mod not found")
        }
      }
      return launcher
    }

}
