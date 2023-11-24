package org.wdt.wdtc.core.download.game;


import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.manger.URLManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.URLUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DownloadVersionGameFile {
  public final Launcher launcher;
  public final DownloadSourceInterface source;
  public final boolean install;

  public DownloadVersionGameFile(Launcher launcher, boolean install) {
    this.launcher = launcher;
    this.source = DownloadSourceManger.getDownloadSource();
    this.install = install;
  }

  public static void DownloadVersionManifestJsonFile() {
    DownloadUtils.StartDownloadTask(DownloadSourceManger.getDownloadSource().getVersionManifestUrl(), FileManger.getVersionManifestFile());
  }

  public void DownloadGameVersionJson() {
    if (!install) {
      return;
    }
    if (DownloadSourceManger.isOfficialDownloadSource()) {
      List<VersionJsonObjectInterface> versionJsonObjectList = new GameVersionList().getVersionList();
      for (VersionJsonObjectInterface versionJsonObjectInterface : versionJsonObjectList) {
        if (versionJsonObjectInterface instanceof GameVersionList.GameVersionJsonObjectImpl versionJsonObject) {
          if (versionJsonObject.getVersionNumber().equals(launcher.getVersionNumber())) {
            DownloadUtils.StartDownloadTask(versionJsonObject.getVersionJsonURL(), launcher.getVersionJson());
          }
        }
      }
    } else {
      URL jsonURL = URLUtils.toURL(String.format(source.getVersionClientUrl(), launcher.getVersionNumber(), "json"));
      DownloadUtils.StartDownloadTask(jsonURL, launcher.getVersionJson());
    }
  }

  public void DownloadGameAssetsListJson() throws IOException {
    GameVersionJsonObject.FileDataObject fileDataObject = launcher.getGameVersionJsonObject().getAssetIndex();
    URL listJsonURL = DownloadSourceManger.isOfficialDownloadSource()
        ? fileDataObject.getListJsonURL()
        : URLUtils.toURL(fileDataObject.getListJsonURL().toString().replace(URLManger.getPistonMetaMojang(), source.getMetaUrl()));
    if (FileUtils.isFileNotExistsAndIsNotSameSize(launcher.getGameAssetsListJson(), fileDataObject.getFileSize())) {
      DownloadUtils.StartDownloadTask(listJsonURL, launcher.getGameAssetsListJson());
    }
  }

  public void DownloadVersionJar() throws IOException {
    GameVersionJsonObject.FileDataObject fileDataObject = launcher.getGameVersionJsonObject().getDownloads().getClient();
    URL JarUrl = DownloadSourceManger.isOfficialDownloadSource()
        ? fileDataObject.getListJsonURL()
        : URLUtils.toURL(String.format(source.getVersionClientUrl(), launcher.getVersionNumber(), "client"));
    if (FileUtils.isFileNotExistsAndIsNotSameSize(launcher.getVersionJar(), fileDataObject.getFileSize())) {
      DownloadUtils.StartDownloadTask(JarUrl, launcher.getVersionJar());
    }
  }

  public DownloadGameClass DownloadGameLibraryFileTask() {
    return new DownloadGameClass(launcher);
  }

  public DownloadGameAssetsFile getDownloadGameAssetsFile() {
    return new DownloadGameAssetsFile(launcher);
  }
}
