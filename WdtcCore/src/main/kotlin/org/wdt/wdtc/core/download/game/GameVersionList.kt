package org.wdt.wdtc.core.download.game

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.manger.FileManger.versionManifestFile
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import java.io.IOException
import java.net.URL
import java.util.*

class GameVersionList : VersionListInterface {
  private val logmaker = GameVersionList::class.java.getWdtcLogger()

  init {
    if (versionManifestFile.isFileNotExists()) {
      DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
    }
  }

  override val versionList: Set<VersionJsonObjectInterface>
    get() {
      val versionList: MutableSet<VersionJsonObjectInterface> = HashSet()
      try {
        val versionJsonArray = versionManifestFile.readFileToJsonObject().getJsonArray("versions")
        versionJsonArray.forEach {
          val versionJsonObject: GameVersionJsonObjectImpl = it.asJsonObject.parseObject()
          if (versionJsonObject.gameType == "release") {
            versionList.add(versionJsonObject)
          }
        }
      } catch (e: IOException) {
        logmaker.error(e.getExceptionMessage())
      }
      return versionList
    }

  class GameVersionJsonObjectImpl : VersionJsonObjectInterface {
    @SerializedName("id")
    override val versionNumber: String? = null

    @SerializedName("type")
    val gameType: String? = null

    @SerializedName("url")
    val versionJsonURL: URL? = null

    @SerializedName("time")
    val time: String? = null

    @SerializedName("releaseTime")
    val releaseTime: String? = null

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as GameVersionJsonObjectImpl
      return versionNumber == that.versionNumber && gameType == that.gameType
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, gameType)
    }
  }
}
