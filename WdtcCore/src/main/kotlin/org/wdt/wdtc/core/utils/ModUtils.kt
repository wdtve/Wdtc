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

object ModUtils {
  private val logmaker = WdtcLogger.getLogger(ModUtils::class.java)

  @JvmStatic
  fun getModTask(launcher: Launcher): Launcher? {
    return try {
      if (launcher.versionJson.isFileNotExists()) {
        return null
      }
      val r = Pattern.compile("(.+?)-(.+?)-(.+)")
      val m = r.matcher(launcher.gameVersionJsonObject.id)
      if (m.find()) {
        val modName = m.group(2)
        val modVersion = m.group(3)
        when (modName) {
          "forge" -> launcher.setForgeModDownloadInfo(ForgeDownloadInfo(launcher, modVersion))
          "fabric" -> launcher.setFabricModInstallInfo(FabricDonwloadInfo(launcher, modVersion))
          "quilt" -> launcher.setQuiltModDownloadInfo(QuiltInstallTask(launcher, modVersion))
        }
      }
      launcher
    } catch (e: IOException) {
      logmaker.error(e)
      null
    }
  }

  @JvmStatic
  fun gameModIsForge(launcher: Launcher): Boolean {
    return launcher.kind == KindOfMod.FORGE
  }

  @JvmStatic
  fun gameModIsFabric(launcher: Launcher): Boolean {
    return launcher.kind == KindOfMod.FABRIC
  }

  @JvmStatic
  fun gameModIsQuilt(launcher: Launcher): Boolean {
    return launcher.kind == KindOfMod.QUILT
  }

  @JvmStatic
  fun getModDownloadInfo(launcher: Launcher): ModDownloadInfoInterface? {
    return if (gameModIsFabric(launcher)) {
      launcher.fabricModInstallInfo
    } else if (gameModIsForge(launcher)) {
      launcher.forgeModDownloadInfo
    } else if (gameModIsQuilt(launcher)) {
      launcher.quiltModDownloadInfo
    } else {
      null
    }
  }

  @JvmStatic
  fun getVersionModInstall(launcher: Launcher, kind: KindOfMod): ModDownloadInfoInterface? {
    return if (kind == KindOfMod.QUILT) {
      launcher.quiltModDownloadInfo
    } else if (kind == KindOfMod.FORGE) {
      launcher.forgeModDownloadInfo
    } else if (kind == KindOfMod.FABRIC) {
      launcher.fabricModInstallInfo
    } else {
      null
    }
  }

  @JvmStatic
  fun getModInstallTask(launcher: Launcher): InstallTaskInterface? {
    val info = getModDownloadInfo(launcher)
    return info?.modInstallTask
  }

  enum class KindOfMod {
    Original,
    FABRIC,
    FABRICAPI,
    FORGE,
    QUILT
  }
}
