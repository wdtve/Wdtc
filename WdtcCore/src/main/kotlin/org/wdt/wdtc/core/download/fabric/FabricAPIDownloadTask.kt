package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionJsonObjectImpl
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionJsonObjectImpl.FilesObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.io.IOException

class FabricAPIDownloadTask(private val launcher: Launcher, val fabricAPIVersionNumber: String?) {

  private var versionJsonObjectInterface: VersionJsonObjectInterface? = null
  private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()

  constructor(launcher: Launcher, versionJsonObjectInterface: VersionJsonObjectInterface) : this(
    launcher,
    versionJsonObjectInterface.versionNumber
  ) {
    this.versionJsonObjectInterface = versionJsonObjectInterface
  }

  @Throws(IOException::class)
  fun startDownloadFabricAPI() {
    val versionJsonObject = versionJsonObjectInterface as FabricAPIVersionJsonObjectImpl?
    if (versionJsonObject == null) {
      versionListUrl.toStrings().parseJsonArray().forEach {
        val newVersionJsonObject: FabricAPIVersionJsonObjectImpl = it.asJsonObject.parseObject()
        if (newVersionJsonObject.versionNumber == fabricAPIVersionNumber) {
          downloadFabricAPITask(newVersionJsonObject.filesObjectList!![0])
        }
      }
    } else {
      downloadFabricAPITask(versionJsonObject.filesObjectList!![0])
    }
  }

  private fun downloadFabricAPITask(filesObject: FilesObject) {
    startDownloadTask(filesObject.jarDownloadURL!!, File(launcher.gameModsPath, filesObject.jarFileName!!))
  }

  override fun toString(): String {
    return "FabricAPIDownloadTask(fabricAPIVersionNumber=$fabricAPIVersionNumber)"
  }

}
