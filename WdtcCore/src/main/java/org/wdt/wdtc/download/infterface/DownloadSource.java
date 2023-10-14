package org.wdt.wdtc.download.infterface;

public interface DownloadSource {
    String MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    String MOJANG_ASSETS = "https://resources.download.minecraft.net/";
    String MOJANG_LIBRARIES = "https://libraries.minecraft.net/";
    String PISTON_META_MOJANG = "https://piston-meta.mojang.com/";
    String PISTON_DATA_MOJANG = "https://piston-data.mojang.com/";

    String getLibraryUrl();

    String getAssetsUrl();

    String getMetaUrl();

    String getDataUrl();

    String getVersionManifestUrl();

    String getFabricMetaUrl();

    String getFabricLibraryUrl();

    String getForgeLibraryMavenUrl();

}
