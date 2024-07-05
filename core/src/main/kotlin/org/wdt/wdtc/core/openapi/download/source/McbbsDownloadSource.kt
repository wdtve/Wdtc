package org.wdt.wdtc.core.openapi.download.source

import org.wdt.wdtc.core.openapi.download.interfaces.DownloadSourceInterface


class McbbsDownloadSource : DownloadSourceInterface {
  override val libraryURL: String
    get() = "https://download.mcbbs.net/maven/"
  override val assetsURL: String
    get() = "https://download.mcbbs.net/assets/"
  override val metaURL: String
    get() = officialURL
  override val dataURL: String
    get() = officialURL
  override val versionManifestURL: String
    get() = "https://download.mcbbs.net/mc/game/version_manifest.json"
  override val fabricMetaURL: String
    get() = "https://download.mcbbs.net/fabric-meta/"
  override val fabricLibraryURL: String
    get() = libraryURL
  override val forgeLibraryMavenURL: String
    get() = libraryURL
  override val versionClientURL: String
    get() = "https://download.mcbbs.net/version/%s/%s"
  override val officialURL: String
    get() = "https://download.mcbbs.net/"

  // TODO: Bmcl quilt api is not available - see https://bmclapidoc.bangbang93.com/
  override val quiltMetaURL: String
    get() = "https://download.mcbbs.net/quilt-meta/"
  override val quiltMavenURL: String
    get() = libraryURL
}
