package org.wdt.wdtc.core.game

import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.manger.GameFileManger
import org.wdt.wdtc.core.manger.setting
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.setModTask
import java.io.File
import java.io.IOException
import java.util.*

class Launcher @JvmOverloads constructor(version: String, here: File = setting.defaultGamePath) :
  GameFileManger(version, here) {

  var fabricModInstallInfo: FabricDonwloadInfo? = null
    set(value) {
      kind = KindOfMod.FABRIC
      field = value
    }

  var forgeModDownloadInfo: ForgeDownloadInfo? = null
    set(value) {
      kind = KindOfMod.FORGE
      field = value
    }


  var quiltModDownloadInfo: QuiltDownloadInfo? = null
    set(value) {
      kind = KindOfMod.FABRIC
      field = value
    }


  var kind = KindOfMod.ORIGINAL


  @Throws(IOException::class)
  fun beforLaunchTask() {
    if (setting.chineseLanguage && FileUtils.isFileNotExists(gameOptionsFile)) {
      val options = IOUtils.toString(Objects.requireNonNull(javaClass.getResourceAsStream("/assets/options.txt")))
      gameOptionsFile.writeStringToFile(options)
    }
  }

  fun cleanKind() {
    kind = KindOfMod.ORIGINAL
  }


  override fun toString(): String {
    return "Launcher(versionNumber=$versionNumber, kind=$kind)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Launcher

    if (versionNumber != other.versionNumber) return false
    if (kind != other.kind) return false
    if (fabricModInstallInfo != other.fabricModInstallInfo) return false
    if (forgeModDownloadInfo != other.forgeModDownloadInfo) return false
    if (quiltModDownloadInfo != other.quiltModDownloadInfo) return false

    return true
  }

  override fun hashCode(): Int {
    var result = fabricModInstallInfo?.hashCode() ?: 0
    result = 31 * result + (forgeModDownloadInfo?.hashCode() ?: 0)
    result = 31 * result + (quiltModDownloadInfo?.hashCode() ?: 0)
    result = 31 * result + kind.hashCode()
    result = 31 * result + versionNumber.hashCode()
    return result
  }

}

val preferredLauncher: Launcher?
  get() = if (setting.preferredVersion != null) Launcher(setting.preferredVersion!!).setModTask() else null
