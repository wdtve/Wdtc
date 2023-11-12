package org.wdt.wdtc.core.download.source;

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
    return getOfficialUrl();
  }

  @Override
  public String getDataUrl() {
    return getOfficialUrl();
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

  @Override
  public String getVersionClientUrl() {
    return "https://bmclapi2.bangbang93.com/version/%s/%s";
  }

  @Override
  public String getOfficialUrl() {
    return "https://bmclapi2.bangbang93.com/";
  }
}
