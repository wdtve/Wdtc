package org.wdt.wdtc.core.download.forge

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

class ForgeVersionList(private val launcher: Launcher) : VersionListInterface {
    val forgeListUrl: String
        get() = "https://bmclapi2.bangbang93.com/forge/minecraft/${launcher.versionNumber}"

    @get:Throws(IOException::class)
    override val versionList: List<VersionJsonObjectInterface>
        get() {
            val versionObjects: MutableList<VersionJsonObjectInterface> = ArrayList()
            val versionList = getURLToString(forgeListUrl).parseJsonArray()
            for (i in 0 until versionList.size()) {
                val versionObject = versionList.getJsonObject(i)
                versionObjects.add(versionObject.parseObject())
            }
            return versionObjects
        }

    class ForgeVersionJsonObjectImpl : VersionJsonObjectInterface {
        @SerializedName("version")
        override val versionNumber: String? = null

        @SerializedName("modified")
        val modified: String? = null

        @SerializedName("mcversion")
        val mcversion: String? = null
        override fun isInstanceofThis(o: Any?): Boolean {
            return o is ForgeVersionJsonObjectImpl
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as ForgeVersionJsonObjectImpl
            return versionNumber == that.versionNumber && mcversion == that.mcversion
        }

        override fun hashCode(): Int {
            return Objects.hash(versionNumber, mcversion)
        }
    }
}
