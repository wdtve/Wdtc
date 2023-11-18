package org.wdt.wdtc.core.download.forge;


import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.DownloadSourceManger;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.ModUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.ZipUtils;
import org.wdt.wdtc.core.utils.gson.JSONObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.IOException;

public class ForgeDownloadInfo implements ModDownloadInfoInterface {
  private static final Logger logmaker = WdtcLogger.getLogger(ForgeDownloadInfo.class);
  protected final DownloadSourceInterface source;
  @Getter
  protected final String ForgeVersionNumber;
  protected final Launcher launcher;


  public ForgeDownloadInfo(Launcher launcher, String forgeVersionNumber) {
    ForgeVersionNumber = forgeVersionNumber;
    this.launcher = launcher;
    this.source = DownloadSourceManger.getDownloadSource();
  }

  public ForgeDownloadInfo(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
  }

  public void DownloadInstallJar() {
    DownloadUtils.StartDownloadTask(getForgeInstallJarUrl(), getForgeInstallJarPath());
  }

  private String getInstallJarUrl() {
    return source.getForgeLibraryMavenUrl() + "net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
  }

  public String getForgeInstallJarPath() {
    return FileManger.getWdtcCache() + "/" + ForgeVersionNumber + "-installer.jar";
  }

  public String getForgeInstallJarUrl() {
    return getInstallJarUrl().replaceAll(":mcversion", launcher.getVersionNumber()).replaceAll(":forgeversion", ForgeVersionNumber);
  }

  public void getInstallProfile() {
    ZipUtils.unZipToFile(getForgeInstallJarPath(), getInstallProfilePath(), "install_profile.json");

  }


  public String getInstallProfilePath() {
    return FileManger.getWdtcCache() + "/install_profile" + "-" + launcher.getVersionNumber() + "-" + ForgeVersionNumber + ".json";
  }

  public JSONObject getInstallPrefileJSONObject() throws IOException {
    return JSONUtils.readFiletoJSONObject(getInstallProfilePath());
  }


  public String getForgeVersionJsonPath() {
    return FileManger.getWdtcCache() + "/version-" + launcher.getVersionNumber() + "-" + ForgeVersionNumber + ".json";
  }

  public void getForgeVersionJson() {
    ZipUtils.unZipToFile(getForgeInstallJarPath(), getForgeVersionJsonPath(), "version.json");
  }

  @SneakyThrows
  public JSONObject getForgeVersionJsonObject() {
    return JSONUtils.readFiletoJSONObject(getForgeVersionJsonPath());
  }


  @Override
  public String getModVersion() {
    return ForgeVersionNumber;
  }

  @Override
  public InstallTaskInterface getModInstallTask() {
    return new ForgeInstallTask(launcher, ForgeVersionNumber);
  }

  @Override
  public ModUtils.KindOfMod getModKind() {
    return ModUtils.KindOfMod.FORGE;
  }
}
