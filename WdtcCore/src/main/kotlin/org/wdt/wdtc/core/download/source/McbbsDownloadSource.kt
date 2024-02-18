package org.wdt.wdtc.core.download.source

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface

class McbbsDownloadSource : DownloadSourceInterface {
  override val libraryUrl: String
    get() = "https://download.mcbbs.net/maven/"
  override val assetsUrl: String
    get() = "https://download.mcbbs.net/assets/"
  override val metaUrl: String
    get() = officialUrl
  override val dataUrl: String
    get() = officialUrl
  override val versionManifestUrl: String
    get() = "https://download.mcbbs.net/mc/game/version_manifest.json"
  override val fabricMetaUrl: String
    get() = "https://download.mcbbs.net/fabric-meta/"
  override val fabricLibraryUrl: String
    get() = libraryUrl
  override val forgeLibraryMavenUrl: String
    get() = libraryUrl
  override val versionClientUrl: String
    get() = "https://download.mcbbs.net/version/%s/%s"
  override val officialUrl: String
    get() = "https://download.mcbbs.net/"

  // TODO: Bmcl quilt api is not available - see https://bmclapidoc.bangbang93.com/
  override val quiltMetaUrl: String
    get() = "https://download.mcbbs.net/quilt-meta/"
  override val quiltMavenUrl: String
    get() = libraryUrl
}
