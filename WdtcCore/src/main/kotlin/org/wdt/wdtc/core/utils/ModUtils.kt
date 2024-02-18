@file:JvmName("ModUtils")

package org.wdt.wdtc.core.utils

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Version
import java.io.IOException
import java.util.regex.Pattern

fun Version.setModTask(): Version? {
  return try {
    if (this.versionJson.isFileNotExists()) {
      return null
    }
    Pattern.compile("(.+?)-(.+?)-(.+)").matcher(this.gameVersionJsonObject.id).let {
      if (it.find()) {
        val modVersion = it.group(3)
        when (it.group(2)) {
          "forge" -> this.forgeModDownloadInfo = ForgeDownloadInfo(this, modVersion)
          "fabric" -> this.fabricModInstallInfo = FabricDonwloadInfo(this, modVersion)
          "quilt" -> this.quiltModDownloadInfo = QuiltInstallTask(this, modVersion)
        }
      }
    }
    this
  } catch (e: IOException) {
    null
  }
}

val Version.isForge: Boolean
  get() = this.kind == KindOfMod.FORGE


val Version.isFabric: Boolean
  get() = this.kind == KindOfMod.FABRIC


val Version.isQuilt
  get() = this.kind == KindOfMod.QUILT


val Version.modDownloadInfo: ModDownloadInfoInterface?
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

val Version.modInstallTask: ModInstallTaskInterface?
  get() = this.modDownloadInfo?.modInstallTask


enum class KindOfMod {
  ORIGINAL,
  FABRIC,
  FABRICAPI,
  FORGE,
  QUILT
}

