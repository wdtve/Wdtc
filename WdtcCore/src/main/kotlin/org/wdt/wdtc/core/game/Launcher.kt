package org.wdt.wdtc.core.game

import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.auth.accounts.Accounts
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.game.config.GameConfig
import org.wdt.wdtc.core.game.config.VersionInfo
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.GameFileManger
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.ModUtils.getModTask
import java.io.File
import java.io.IOException
import java.util.*

class Launcher @JvmOverloads constructor(version: String?, here: File? = setting.defaultGamePath) : GameFileManger(
    version!!, here!!
) {
    @JvmField
    var fabricModInstallInfo: FabricDonwloadInfo? = null
    var kind = KindOfMod.Original

    @JvmField
    var forgeModDownloadInfo: ForgeDownloadInfo? = null

    @JvmField
    var quiltModDownloadInfo: QuiltDownloadInfo? = null
    fun setQuiltModDownloadInfo(quiltModDownloadInfo: QuiltInstallTask?) {
        kind = KindOfMod.QUILT
        this.quiltModDownloadInfo = quiltModDownloadInfo
    }

    fun setFabricModInstallInfo(fabricModInstallInfo: FabricDonwloadInfo?) {
        kind = KindOfMod.FABRIC
        this.fabricModInstallInfo = fabricModInstallInfo
    }

    fun setForgeModDownloadInfo(forgeModDownloadInfo: ForgeDownloadInfo?) {
        kind = KindOfMod.FORGE
        this.forgeModDownloadInfo = forgeModDownloadInfo
    }

    val gameConfig: GameConfig
        get() = GameConfig(this)

    @Throws(IOException::class)
    fun beforLaunchTask() {
        if (setting.chineseLanguage && FileUtils.isFileNotExists(gameOptionsFile)) {
            val options = IOUtils.toString(Objects.requireNonNull(javaClass.getResourceAsStream("/assets/options.txt")))
            gameOptionsFile.writeStringToFile(options)
        }
    }

    fun cleanKind() {
        kind = KindOfMod.Original
    }

    val accounts: Accounts
        get() = Accounts()
    val versionInfo: VersionInfo
        get() = VersionInfo(this)
    val gameDirectoryManger: GameDirectoryManger
        get() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Launcher) return false
        return if (other.versionNumber == versionNumber) {
            other.kind === kind
        } else false
    }

    override fun hashCode(): Int {
        return Objects.hash(versionNumber, kind)
    }

    override fun toString(): String {
        return "Launcher{" +
                "version=" + versionNumber +
                ",kind=" + kind +
                '}'
    }

    companion object {
        @JvmStatic
        val preferredLauncher: Launcher?
            get() {
                val setting = setting
                return if (setting.preferredVersion != null) getModTask(Launcher(setting.preferredVersion)) else null
            }
    }
}
