package org.wdt.wdtc.core.game.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface;
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.ModUtils;

@Setter
@Getter
@ToString
public class VersionInfo {
  @SerializedName("GameVersionNumber")
  private String VersionNumber;
  @SerializedName("ModKind")
  private ModUtils.KindOfMod kind;
  @SerializedName("ModVersionNumber")
  private String ModVersion;

  public VersionInfo(Launcher launcher) {
    this.VersionNumber = launcher.getVersionNumber();
    this.kind = launcher.getKind();
    ModDownloadInfoInterface info = ModUtils.getModDownloadInfo(launcher);
    if (info != null) {
      this.ModVersion = info.getModVersion();
    } else {
      this.ModVersion = null;
    }
  }


  public Launcher getLauncher() {
    Launcher launcher = new Launcher(getVersionNumber());
    switch (getKind()) {
      case FORGE -> launcher.setForgeModDownloadInfo(new ForgeDownloadInfo(launcher, getModVersion()));
      case FABRIC -> launcher.setFabricModInstallInfo(new FabricDonwloadInfo(launcher, getVersionNumber()));
      case QUILT -> launcher.setQuiltModDownloadInfo(new QuiltInstallTask(launcher, getVersionNumber()));
    }
    return launcher;
  }
}
