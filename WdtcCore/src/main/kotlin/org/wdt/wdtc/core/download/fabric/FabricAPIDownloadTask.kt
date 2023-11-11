package org.wdt.wdtc.core.download.fabric

import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionJsonObjectImpl
import org.wdt.wdtc.core.download.fabric.FabricAPIVersionList.FabricAPIVersionJsonObjectImpl.FilesObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.DownloadUtils.Companion.startDownloadTask
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.File
import java.io.IOException

class FabricAPIDownloadTask(private val launcher: Launcher, val fabricAPIVersionNumber: String?) {
    private var versionJsonObjectInterface: VersionJsonObjectInterface? = null
    private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version"

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
            val versionListArray = getURLToString(versionListUrl).parseJsonArray()
            for (i in 0 until versionListArray.size()) {
                val versionObject = versionListArray.getJsonObject(i)
                val newVersionJsonObject: FabricAPIVersionJsonObjectImpl = versionObject.parseObject()
                if (newVersionJsonObject.versionNumber == fabricAPIVersionNumber) {
                    downloadFabricAPITask(newVersionJsonObject.filesObjectList!![0])
                }
            }
        } else {
            downloadFabricAPITask(versionJsonObject.filesObjectList!![0])
        }
    }

    fun downloadFabricAPITask(filesObject: FilesObject) {
        startDownloadTask(filesObject.jarDownloadURL!!, File(launcher.gameModsPath, filesObject.jarFileName))
    }
}
