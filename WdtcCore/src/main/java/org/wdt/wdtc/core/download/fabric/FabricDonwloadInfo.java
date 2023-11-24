package org.wdt.wdtc.core.download.fabric;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.ModUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

public class FabricDonwloadInfo implements ModDownloadInfoInterface {
  private static final Logger logmaker = WdtcLogger.getLogger(FabricDonwloadInfo.class);
  @Getter
  protected final String FabricVersionNumber;
  protected final Launcher launcher;
  @Getter
  @Setter
  private FabricAPIDownloadTask ApiDownloadTask;


  public FabricDonwloadInfo(Launcher launcher, String FabricVersionNumber) {
    this.FabricVersionNumber = FabricVersionNumber;
    this.launcher = launcher;
  }

  public FabricDonwloadInfo(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
  }

  public String getFabricVersionFileUrl() {
    return DownloadSourceManger.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/json";
  }


  public void DownloadProfileZip() {
    DownloadUtils.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
  }

  public String getProfileZipFile() {
    return String.format(FileManger.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersionNumber(), getFabricVersionNumber());
  }

  public String getProfileZipUrl() {
    return String.format(DownloadSourceManger.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/zip", launcher.getVersionNumber(), getFabricVersionNumber());
  }

  public String FromFabricLoaderFolder() {
    return String.format("fabric-loader-%s-%s", getFabricVersionNumber(), launcher.getVersionNumber());
  }

  public File getFabricJar() {
    return new File(FileManger.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".jar");
  }

  public File getFabricVersionJson() {
    return new File(String.format(FileManger.getWdtcCache() + "/%s-fabric-%s.json", launcher.getVersionNumber(), FabricVersionNumber));
  }

  public JsonObject getFabricVersionJsonObject() throws IOException {
    return JsonUtils.getJsonObject(getFabricVersionJson());
  }


  public boolean isAPIDownloadTaskNoNull() {
    return ApiDownloadTask != null;
  }

  @Override
  public String getModVersion() {
    return FabricVersionNumber;
  }

  @Override
  public InstallTaskInterface getModInstallTask() {
    return new FabricInstallTask(launcher, FabricVersionNumber);
  }

  @Override
  public ModUtils.KindOfMod getModKind() {
    return ModUtils.KindOfMod.FABRIC;
  }
}
