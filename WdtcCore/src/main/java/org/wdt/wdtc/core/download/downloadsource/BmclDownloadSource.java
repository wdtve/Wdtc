package org.wdt.wdtc.core.download.downloadsource;

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;

public class BmclDownloadSource implements DownloadSourceInterface {
    @Override
    public String getLibraryUrl() {
        return "https://bmclapi2.bangbang93.com/maven/";
    }

    @Override
    public String getAssetsUrl() {
        return "https://bmclapi2.bangbang93.com/assets/";
    }

    @Override
    public String getMetaUrl() {
        return "https://bmclapi2.bangbang93.com/";
    }

    @Override
    public String getDataUrl() {
        return "https://bmclapi2.bangbang93.com/";
    }

    @Override
    public String getVersionManifestUrl() {
        return "https://bmclapi2.bangbang93.com/mc/game/version_manifest.json";
    }

    @Override
    public String getFabricMetaUrl() {
        return "https://bmclapi2.bangbang93.com/fabric-meta/";
    }

    @Override
    public String getFabricLibraryUrl() {
        return "https://bmclapi2.bangbang93.com/maven/";
    }

    @Override
    public String getForgeLibraryMavenUrl() {
        return "https://bmclapi2.bangbang93.com/maven/";
    }
}
