package org.wdt.wdtc.core.game

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.wdt.utils.dependency.DependencyDownload
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.net.URL

class LibraryObject {
    @SerializedName("downloads")
    var downloads: Downloads? = null

    @SerializedName("name")
    var libraryName: String? = null

    @SerializedName("rules")
    var rules: JsonArray? = null

    @SerializedName("natives")
    var natives: JsonObject? = null

    class Downloads {
        @SerializedName("artifact")
        var artifact: Artifact? = null

        @SerializedName("classifiers")
        var classifiers: Classifiers? = null
    }

    class Artifact {
        var path: String? = null
        var sha1: String? = null
        var size: Long = 0
        var url: URL? = null
    }

    class Classifiers {
        @SerializedName("natives-macos")
        var nativesMacos: NativesOs? = null

        @SerializedName("natives-linux")
        var nativesLinux: NativesOs? = null

        @SerializedName("natives-windows")
        var nativesindows: NativesOs? = null
    }

    class NativesOs {
        var path: String? = null
        var sha1: String? = null
        var size = 0
        var url: URL? = null
    }

    companion object {
        private val logger = getLogger(LibraryObject::class.java)
        fun getLibraryObject(dependency: DependencyDownload, defaultUrl: String?): LibraryObject {
            val artifact = Artifact()
            val url = dependency.libraryUrl
            artifact.sha1 = IOUtils.getInputStreamSha1(url.openStream())
            artifact.path = dependency.formJar()
            artifact.size = url.openConnection().contentLengthLong
            dependency.defaultUrl = defaultUrl
            artifact.url = dependency.libraryUrl
            val downloads = Downloads()
            downloads.artifact = artifact
            val libraryObject = LibraryObject()
            libraryObject.libraryName = dependency.libraryName
            libraryObject.downloads = downloads
            logger.info(libraryObject)
            return libraryObject
        }

        @JvmStatic
        fun getLibraryObject(jsonObject: JsonObject): LibraryObject {
            return jsonObject.parseObject()
        }

        fun getLibraryObject(jsonStr: String): LibraryObject {
            return jsonStr.parseObject()
        }
    }
}
