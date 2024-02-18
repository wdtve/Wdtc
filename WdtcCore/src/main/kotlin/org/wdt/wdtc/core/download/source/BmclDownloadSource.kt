package org.wdt.wdtc.core.download.source

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface

class BmclDownloadSource : DownloadSourceInterface {
  override val libraryUrl: String
    get() = "https://bmclapi2.bangbang93.com/maven/"
  override val assetsUrl: String
    get() = "https://bmclapi2.bangbang93.com/assets/"
  override val metaUrl: String
    get() = officialUrl
  override val dataUrl: String
    get() = officialUrl
  override val versionManifestUrl: String
    get() = "https://bmclapi2.bangbang93.com/mc/game/version_manifest.json"
  override val fabricMetaUrl: String
    get() = "https://bmclapi2.bangbang93.com/fabric-meta/"
  override val fabricLibraryUrl: String
    get() = libraryUrl
  override val forgeLibraryMavenUrl: String
    get() = libraryUrl
  override val versionClientUrl: String
    get() = "https://bmclapi2.bangbang93.com/version/%s/%s"
  override val officialUrl: String
    get() = "https://bmclapi2.bangbang93.com/"
  // TODO: Bmcl quilt api is not available - see https://bmclapidoc.bangbang93.com/
  override val quiltMetaUrl: String
    get() = "https://bmclapi2.bangbang93.com/quilt-meta/"
  override val quiltMavenUrl: String
    get() = libraryUrl

}
