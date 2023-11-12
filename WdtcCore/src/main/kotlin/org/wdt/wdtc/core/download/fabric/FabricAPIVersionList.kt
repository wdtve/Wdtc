package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.IOException
import java.net.URL
import java.util.*

class FabricAPIVersionList(private val launcher: Launcher) : VersionListInterface {
  private val versionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version"

  @get:Throws(IOException::class)
  override val versionList: List<VersionJsonObjectInterface>
    get() {
      val versionList: MutableList<VersionJsonObjectInterface> = ArrayList()
      val versionListArray = getURLToString(versionListUrl).parseJsonArray()
      for (i in 0 until versionListArray.size()) {
        val versionObject = versionListArray.getJsonObject(i)
        val versionJsonObject: FabricAPIVersionJsonObjectImpl = versionObject.parseObject()
        if (launcher.versionNumber == versionJsonObject.gameVersion!![0]) {
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
    override fun isInstanceofThis(o: Any?): Boolean {
      return o is FabricAPIVersionJsonObjectImpl
    }

    override fun equals(o: Any?): Boolean {
      if (this === o) return true
      if (o == null || javaClass != o.javaClass) return false
      val that = o as FabricAPIVersionJsonObjectImpl
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
