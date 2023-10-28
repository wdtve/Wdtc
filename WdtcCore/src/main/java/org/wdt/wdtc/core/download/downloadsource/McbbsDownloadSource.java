package org.wdt.wdtc.core.download.downloadsource;

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;

public class McbbsDownloadSource implements DownloadSourceInterface {
    @Override
    public String getLibraryUrl() {
        return "https://download.mcbbs.net/maven/";
    }

    @Override
    public String getAssetsUrl() {
        return "https://download.mcbbs.net/assets/";
    }

    @Override
    public String getMetaUrl() {
        return "https://download.mcbbs.net/";
    }

    @Override
    public String getDataUrl() {
        return "https://download.mcbbs.net/";
    }

    @Override
    public String getVersionManifestUrl() {
        return "https://download.mcbbs.net/mc/game/version_manifest.json";
    }

    @Override
    public String getFabricMetaUrl() {
        return "https://download.mcbbs.net/fabric-meta/";
    }

    @Override
    public String getFabricLibraryUrl() {
        return "https://download.mcbbs.net/maven/";
    }

    @Override
    public String getForgeLibraryMavenUrl() {
        return "https://download.mcbbs.net/maven/";
    }
}
