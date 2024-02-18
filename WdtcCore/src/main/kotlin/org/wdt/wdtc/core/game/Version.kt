package org.wdt.wdtc.core.game

import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.manger.GameFileManger
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.utils.KindOfMod
import org.wdt.wdtc.core.utils.setModTask
import java.io.File
import java.util.*

class Version @JvmOverloads constructor(versionNumber: String, here: File = currentSetting.defaultGamePath) :
  GameFileManger(versionNumber, here) {

  var kind = KindOfMod.ORIGINAL

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


  fun cleanKind() {
    kind = KindOfMod.ORIGINAL
  }


  override fun toString(): String {
    return "Launcher(versionNumber=$versionNumber, kind=$kind)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Version

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

val preferredVersion: Version?
  get() = currentSetting.preferredVersion.run {
    this?.setModTask()
  }

class GameVersionsList(
  private val versions: LinkedList<Version> = LinkedList()
) : MutableList<Version> by versions {
  override fun add(element: Version): Boolean {
    element.setModTask().let {
      if (it != null && it.versionJson.isFileExists()) {
        return versions.add(it)
      }
    }
    return false
  }

  fun Version.addToList() {
    add(this)
  }
}

