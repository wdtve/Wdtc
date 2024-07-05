package org.wdt.wdtc.core.openapi.download.source

import org.wdt.wdtc.core.openapi.download.interfaces.DownloadSourceInterface


class BmclDownloadSource : DownloadSourceInterface {
  override val libraryURL: String
    get() = "https://bmclapi2.bangbang93.com/maven/"
  override val assetsURL: String
    get() = "https://bmclapi2.bangbang93.com/assets/"
  override val metaURL: String
    get() = officialURL
  override val dataURL: String
    get() = officialURL
  override val versionManifestURL: String
    get() = "https://bmclapi2.bangbang93.com/mc/game/version_manifest.json"
  override val fabricMetaURL: String
    get() = "https://bmclapi2.bangbang93.com/fabric-meta/"
  override val fabricLibraryURL: String
    get() = libraryURL
  override val forgeLibraryMavenURL: String
    get() = libraryURL
  override val versionClientURL: String
    get() = "https://bmclapi2.bangbang93.com/version/%s/%s"
  override val officialURL: String
    get() = "https://bmclapi2.bangbang93.com/"
  // TODO: Bmcl quilt api is not available - see https://bmclapidoc.bangbang93.com/
  override val quiltMetaURL: String
    get() = "https://bmclapi2.bangbang93.com/quilt-meta/"
  override val quiltMavenURL: String
    get() = libraryURL

}
