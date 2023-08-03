package org.wdt.wdtc.download.infterface;

public interface DownloadSource {
    String getLibraryUrl();

    String getAssetsUrl();

    String getMetaUrl();

    String getDataUrl();

    String getVersionManifestUrl();

    String getFabricMetaUrl();

    String getFabricLibraryUrl();

    String getForgeLibraryMavenUrl();

}
