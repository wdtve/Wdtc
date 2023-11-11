package org.wdt.wdtc.core.download.quilt

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.game.*
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.IOException
import java.util.*

class QuiltVersionList(private val launcher: Launcher) : VersionListInterface {
    private val quiltquVersionListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s"

    @get:Throws(IOException::class)
    override val versionList: List<VersionJsonObjectInterface>
        get() {
            val list: MutableList<VersionJsonObjectInterface> = ArrayList()
            val versionArray = getURLToString(quiltquVersionListUrl.format(launcher.versionNumber)).parseJsonArray()
            for (i in 0 until versionArray.size()) {
                val versionObject = versionArray.getJsonObject(i)
                list.add(versionObject.getJsonObject("loader").parseObject())
            }
            return list
        }

    class QuiltVersionJsonObjectImpl : VersionJsonObjectInterface {
        @SerializedName("version")
        override var versionNumber: String? = null

        @SerializedName("build")
        var buildNumber = 0
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as QuiltVersionJsonObjectImpl
            return buildNumber == that.buildNumber && versionNumber == that.versionNumber
        }

        override fun hashCode(): Int {
            return Objects.hash(versionNumber, buildNumber)
        }

        override fun isInstanceofThis(o: Any?): Boolean {
            return o is QuiltVersionJsonObjectImpl
        }
    }

}
