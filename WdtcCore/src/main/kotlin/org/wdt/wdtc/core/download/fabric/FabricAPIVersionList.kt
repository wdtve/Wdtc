package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toStrings
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.toURL
import java.io.IOException
import java.net.URL
import java.util.*

class FabricAPIVersionList(private val launcher: Launcher) : VersionListInterface {
  private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version".toURL()

  @get:Throws(IOException::class)
  override val versionList: List<VersionJsonObjectInterface>
    get() {
      val versionList: MutableList<VersionJsonObjectInterface> = ArrayList()
      val versionListArray = versionListUrl.toStrings().parseJsonArray()
      versionListArray.forEach {
        val versionJsonObject: FabricAPIVersionJsonObjectImpl = it.asJsonObject.parseObject()
        if (launcher.versionNumber == (versionJsonObject.gameVersion?.get(0) ?: throw NullPointerException())) {
          versionList.add(versionJsonObject)
        }
      }
      return versionList
    }

  class FabricAPIVersionJsonObjectImpl : VersionJsonObjectInterface {
    @SerializedName("version_number")
    override val versionNumber: String? = null

    @SerializedName("files")
    val filesObjectList: List<FilesObject>? = null

    @SerializedName("game_versions")
    val gameVersion: List<String>? = null

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as FabricAPIVersionJsonObjectImpl
      return versionNumber == that.versionNumber
    }

    override fun hashCode(): Int {
      return Objects.hash(versionNumber)
    }

    class FilesObject {
      @SerializedName("url")
      val jarDownloadURL: URL? = null

      @SerializedName("size")
      val fileSize = 0

      @SerializedName("filename")
      val jarFileName: String? = null
    }
  }
}
