package org.wdt.wdtc.core.download.game

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface
import org.wdt.wdtc.core.manger.versionManifestFile
import org.wdt.wdtc.core.utils.error
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker
import java.io.IOException
import java.net.URL
import java.util.*

class GameVersionList : VersionListInterface {

  init {
    if (versionManifestFile.isFileNotExists()) {
      DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
    }
  }

  override val versionList: GameVersionsObjectList
    get() {
      return GameVersionsObjectList().apply {
        try {
          versionManifestFile.readFileToJsonObject().getJsonArray("versions").forEach {
            it.asJsonObject.parseObject<GameVersionsJsonObjectImpl>().let { version ->
              if (version.gameType == "release") {
                add(version)
              }
            }
          }
        } catch (e: IOException) {
          logmaker.error(e.getExceptionMessage())
        }
      }
    }

  class GameVersionsJsonObjectImpl(
    @field:SerializedName("id")
    override val versionNumber: String,

    @field:SerializedName("releaseTime")
    val releaseTime: Date
  ) : VersionsJsonObjectInterface, Comparable<GameVersionsJsonObjectImpl> {

    @SerializedName("type")
    val gameType: String? = null

    @SerializedName("url")
    val versionJsonURL: URL? = null

    @SerializedName("time")
    val time: Date? = null
    override fun compareTo(other: GameVersionsJsonObjectImpl): Int {
      return releaseTime.compareTo(other.releaseTime)
    }


    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as GameVersionsJsonObjectImpl
      return versionNumber == that.versionNumber && gameType == that.gameType
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber, gameType)
    }

  }
}

class GameVersionsObjectList(
  private val versionsList: TreeSet<VersionsJsonObjectInterface> = TreeSet()
) : MutableSet<VersionsJsonObjectInterface> by versionsList {
  constructor(collection: Collection<VersionsJsonObjectInterface>) : this(TreeSet(collection))

  override fun toString(): String {
    return "GameVersionsObjectList(versionsList=$versionsList)"
  }
}