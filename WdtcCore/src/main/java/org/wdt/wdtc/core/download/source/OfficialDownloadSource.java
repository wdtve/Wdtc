package org.wdt.wdtc.core.download.source;

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;

public class OfficialDownloadSource implements DownloadSourceInterface {
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

    @Override
    public String getVersionClientUrl() {
        throw new RuntimeException("Official doesn't have version client url");
    }

    @Override
    public String getOfficialUrl() {
        return getMetaUrl();
    }


}

