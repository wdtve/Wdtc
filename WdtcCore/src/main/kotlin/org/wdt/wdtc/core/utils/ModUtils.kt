@file:JvmName("ModUtils")

package org.wdt.wdtc.core.utils

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Launcher
import java.io.IOException
import java.util.regex.Pattern

fun Launcher.setModTask(): Launcher? {
  return try {
    if (this.versionJson.isFileNotExists()) {
      return null
    }
    val m = Pattern.compile("(.+?)-(.+?)-(.+)").matcher(this.gameVersionJsonObject.id!!)
    if (m.find()) {
      val modName = m.group(2)
      val modVersion = m.group(3)
      when (modName) {
        "forge" -> this.forgeModDownloadInfo = ForgeDownloadInfo(this, modVersion)
        "fabric" -> this.fabricModInstallInfo = FabricDonwloadInfo(this, modVersion)
        "quilt" -> this.quiltModDownloadInfo = QuiltInstallTask(this, modVersion)
      }
    }
    this
  } catch (e: IOException) {
    null
  }
}

val Launcher.isForge: Boolean
  get() = this.kind == KindOfMod.FORGE


val Launcher.isFabric: Boolean
  get() = this.kind == KindOfMod.FABRIC


val Launcher.isQuilt
  get() = this.kind == KindOfMod.QUILT


val Launcher.modDownloadInfo: ModDownloadInfoInterface?
  get() {
    return if (this.isFabric) {
      this.fabricModInstallInfo
    } else if (this.isForge) {
      this.forgeModDownloadInfo
    } else if (this.isQuilt) {
      this.quiltModDownloadInfo
    } else {
      null
    }
  }

val Launcher.modInstallTask: InstallTaskInterface?
  get() = this.modDownloadInfo?.modInstallTask


enum class KindOfMod {
  ORIGINAL,
  FABRIC,
  FABRICAPI,
  FORGE,
  QUILT
}

