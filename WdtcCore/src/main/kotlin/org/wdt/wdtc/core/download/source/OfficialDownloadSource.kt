package org.wdt.wdtc.core.download.source

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface

class OfficialDownloadSource : DownloadSourceInterface {
    override val libraryUrl: String
        get() = "https://libraries.minecraft.net/"
    override val assetsUrl: String
        get() = "https://resources.download.minecraft.net/"
    override val metaUrl: String
        get() = "https://piston-meta.mojang.com/"
    override val dataUrl: String
        get() = "https://piston-data.mojang.com/"
    override val versionManifestUrl: String
        get() = "https://piston-meta.mojang.com/mc/game/version_manifest.json"
    override val fabricMetaUrl: String
        get() = "https://meta.fabricmc.net/"
    override val fabricLibraryUrl: String
        get() = "https://maven.fabricmc.net/"
    override val forgeLibraryMavenUrl: String
        get() = "https://files.minecraftforge.net/maven/"
    override val versionClientUrl: String
        get() {
            throw RuntimeException("Official doesn't have version client url")
        }
    override val officialUrl: String
        get() = metaUrl
}
