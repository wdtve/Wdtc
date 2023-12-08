package org.wdt.wdtc.core.game

import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.manger.GameFileManger
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.ModUtils.KindOfMod
import org.wdt.wdtc.core.utils.ModUtils.setModTask
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
    return "Launcher(versionNumber=$versionNumber, kind=$kind)"
  }


  companion object {
    val preferredLauncher: Launcher?
      get() = if (setting.preferredVersion != null) Launcher(setting.preferredVersion!!).setModTask() else null

  }
}
