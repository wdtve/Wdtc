package org.wdt.wdtc.download.downloadsource;

import org.wdt.wdtc.download.infterface.DownloadSource;

public class OfficialDownloadSource implements DownloadSource {
    @Override
    public String getLibraryUrl() {
        return "https://libraries.minecraft.net/";
    }

    @Override
    public String getAssetsUrl() {
        return "https://resources.download.minecraft.net/";
    }

    @Override
    public String getMetaUrl() {
        return "https://piston-meta.mojang.com/";
    }

    @Override
    public String getDataUrl() {
        return "https://piston-data.mojang.com/";
    }

    @Override
    public String getVersionManifestUrl() {
        return "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public String getFabricMetaUrl() {
        return "https://meta.fabricmc.net/";
    }

    @Override
    public String getFabricLibraryUrl() {
        return "https://maven.fabricmc.net/";
    }

    @Override
    public String getForgeLibraryMavenUrl() {
        return "https://files.minecraftforge.net/maven/";
    }


}

