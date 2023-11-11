package org.wdt.wdtc.core.game.config

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.ModUtils.getModDownloadInfo

class VersionInfo(launcher: Launcher) {
    @SerializedName("GameVersionNumber")
    val versionNumber: String

    @SerializedName("ModKind")
    val kind: KindOfMod

    @SerializedName("ModVersionNumber")
    var modVersion: String? = null

    init {
        versionNumber = launcher.versionNumber
        kind = launcher.kind
        modVersion = getModDownloadInfo(launcher)?.modVersion
    }

    val launcher: Launcher
        get() {
            val launcher = Launcher(versionNumber)
            when (kind) {
                KindOfMod.FORGE -> launcher.setForgeModDownloadInfo(ForgeDownloadInfo(launcher, modVersion))
                KindOfMod.FABRIC -> launcher.setFabricModInstallInfo(FabricDonwloadInfo(launcher, modVersion))
                KindOfMod.QUILT -> launcher.setQuiltModDownloadInfo(QuiltInstallTask(launcher, modVersion))
                else -> {}
            }
            return launcher
        }
}
