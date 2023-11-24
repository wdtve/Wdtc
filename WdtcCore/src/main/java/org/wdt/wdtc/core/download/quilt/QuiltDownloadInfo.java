package org.wdt.wdtc.core.download.quilt;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface;
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.ModUtils;

import java.io.File;
import java.io.IOException;

public class QuiltDownloadInfo implements ModDownloadInfoInterface {
  private static final String LibraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s/profile/json";

  protected final Launcher launcher;
  @Getter
  protected final String QuiltVersionNumber;

  public QuiltDownloadInfo(Launcher launcher, VersionJsonObjectInterface versionJsonObjectInterface) {
    this(launcher, versionJsonObjectInterface.getVersionNumber());
  }

  public QuiltDownloadInfo(Launcher launcher, String quiltVersionNumber) {
    this.launcher = launcher;
    QuiltVersionNumber = quiltVersionNumber;
  }

  public File getQuiltVersionJson() {
    return new File(FileManger.getWdtcCache(), launcher.getVersionNumber() + "-quilt-" + QuiltVersionNumber + ".json");
  }

  public String getQuiltVersionJsonUrl() {
    return String.format(LibraryListUrl, launcher.getVersionNumber(), QuiltVersionNumber);
  }

  public JsonObject getQuiltGameVersionJsonObject() throws IOException {
    return JsonUtils.getJsonObject(getQuiltVersionJson());
  }

  @Override
  public String getModVersion() {
    return QuiltVersionNumber;
  }

  @Override
  public InstallTaskInterface getModInstallTask() {
    return new QuiltInstallTask(launcher, QuiltVersionNumber);
  }

  @Override
  public ModUtils.KindOfMod getModKind() {
    return ModUtils.KindOfMod.QUILT;
  }
}
