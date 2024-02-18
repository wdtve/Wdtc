package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionsJsonObjectImpl
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionsJsonObjectImpl.FilesObject
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.io.IOException

class FabricAPIDownloadTask(private val version: Version, val fabricAPIVersionNumber: String?) {

  private var versionsJsonObjectInterface: VersionsJsonObjectInterface? = null
  private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()

  constructor(version: Version, versionsJsonObjectInterface: VersionsJsonObjectInterface) : this(
    version,
    versionsJsonObjectInterface.versionNumber
  ) {
    this.versionsJsonObjectInterface = versionsJsonObjectInterface
  }

  @Throws(IOException::class)
  fun startDownloadFabricAPI() {
    val versionJsonObject = versionsJsonObjectInterface as FabricAPIVersionsJsonObjectImpl?
    if (versionJsonObject == null) {
      versionListUrl.toStrings().parseJsonArray().forEach {
        val newVersionJsonObject: FabricAPIVersionsJsonObjectImpl = it.asJsonObject.parseObject()
        if (newVersionJsonObject.versionNumber == fabricAPIVersionNumber) {
          downloadFabricAPITask(newVersionJsonObject.filesObjectList.ckeckIsNull()[0])
        }
      }
    } else {
      downloadFabricAPITask(versionJsonObject.filesObjectList.ckeckIsNull()[0])
    }
  }

  private fun downloadFabricAPITask(filesObject: FilesObject) {
    startDownloadTask(filesObject.jarDownloadURL!!, File(version.gameModsPath, filesObject.jarFileName!!))
  }

  override fun toString(): String {
    return "FabricAPIDownloadTask(fabricAPIVersionNumber=$fabricAPIVersionNumber)"
  }

}
