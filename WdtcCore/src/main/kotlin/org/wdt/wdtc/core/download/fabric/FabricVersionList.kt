package org.wdt.wdtc.core.download.fabric

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.parseJsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.utils.URLUtils.getURLToString
import java.io.IOException
import java.util.*

class FabricVersionList : VersionListInterface {
    @get:Throws(IOException::class)
    override val versionList: List<VersionJsonObjectInterface>
        get() {
            val fabricVersionList: MutableList<VersionJsonObjectInterface> = ArrayList()
            val list = getURLToString(downloadSource.fabricMetaUrl + "v2/versions/loader").parseJsonArray()
            for (i in 0 until list.size()) {
                val fabricObject = list.getJsonObject(i)
                fabricVersionList.add(fabricObject.parseObject())
            }
            return fabricVersionList
        }

    class FabricVersionJsonObjectImpl : VersionJsonObjectInterface {
        @SerializedName("version")
        override val versionNumber: String? = null

        @SerializedName("build")
        var buildNumber = 0
        override fun isInstanceofThis(o: Any?): Boolean {
            return o is FabricVersionJsonObjectImpl
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as FabricVersionJsonObjectImpl
            return buildNumber == that.buildNumber && versionNumber == that.versionNumber
        }

        override fun hashCode(): Int {
            return Objects.hash(versionNumber, buildNumber)
        }
    }
}
