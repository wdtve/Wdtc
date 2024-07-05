package org.wdt.wdtc.core.openapi.download.source

import org.wdt.wdtc.core.openapi.download.interfaces.DownloadSourceInterface

class OfficialDownloadSource : DownloadSourceInterface {
  override val libraryURL: String
    get() = "https://libraries.minecraft.net/"
  override val assetsURL: String
    get() = "https://resources.download.minecraft.net/"
  override val metaURL: String
    get() = "https://piston-meta.mojang.com/"
  override val dataURL: String
    get() = "https://piston-data.mojang.com/"
  override val versionManifestURL: String
    get() = "https://piston-meta.mojang.com/mc/game/version_manifest.json"
  override val fabricMetaURL: String
    get() = "https://meta.fabricmc.net/"
  override val fabricLibraryURL: String
    get() = "https://maven.fabricmc.net/"
  override val forgeLibraryMavenURL: String
    get() = "https://files.minecraftforge.net/maven/"
  override val versionClientURL: String
    get() {
      throw RuntimeException("Official doesn't have version client url")
    }
  override val officialURL: String
    get() = metaURL
  override val quiltMetaURL: String
    get() = "https://meta.quiltmc.org/"
  override val quiltMavenURL: String
    get() = "https://maven.quiltmc.org/repository/release/"


}
