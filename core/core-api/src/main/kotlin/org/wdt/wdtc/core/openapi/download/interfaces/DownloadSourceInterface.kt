package org.wdt.wdtc.core.openapi.download.interfaces

interface DownloadSourceInterface {
  val libraryUrl: String
  val assetsUrl: String
  val metaUrl: String
  val dataUrl: String
  val versionManifestUrl: String
  val fabricMetaUrl: String
  val fabricLibraryUrl: String
  val forgeLibraryMavenUrl: String
  val versionClientUrl: String
  val officialUrl: String
  val quiltMetaUrl: String
  val quiltMavenUrl: String

  companion object {
    const val MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json"
    const val MOJANG_ASSETS = "https://resources.download.minecraft.net/"
    const val MOJANG_LIBRARIES = "https://libraries.minecraft.net/"
    const val PISTON_META_MOJANG = "https://piston-meta.mojang.com/"
    const val PISTON_DATA_MOJANG = "https://piston-data.mojang.com/"
  }
}
