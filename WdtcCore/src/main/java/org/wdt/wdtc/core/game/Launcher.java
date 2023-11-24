package org.wdt.wdtc.core.game;


import lombok.Getter;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.core.auth.accounts.Accounts;
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo;
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.core.game.config.GameConfig;
import org.wdt.wdtc.core.game.config.VersionInfo;
import org.wdt.wdtc.core.manger.GameDirectoryManger;
import org.wdt.wdtc.core.manger.GameFileManger;
import org.wdt.wdtc.core.manger.SettingManger;
import org.wdt.wdtc.core.utils.ModUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Getter
public class Launcher extends GameFileManger {
  private FabricDonwloadInfo FabricModInstallInfo;
  private ModUtils.KindOfMod kind = ModUtils.KindOfMod.Original;
  private ForgeDownloadInfo ForgeModDownloadInfo;
  private QuiltDownloadInfo QuiltModDownloadInfo;

  public Launcher(String version) {
    this(version, SettingManger.getSetting().getDefaultGamePath());
  }

  public Launcher(String version, File here) {
    super(version, here);
  }

  public static Launcher getPreferredLauncher() {
    SettingManger.Setting setting = SettingManger.getSetting();
    return setting.getPreferredVersion() != null
        ? ModUtils.getModTask(new Launcher(setting.getPreferredVersion()))
        : null;
  }

  public void setQuiltModDownloadInfo(QuiltInstallTask quiltModDownloadInfo) {
    kind = ModUtils.KindOfMod.QUILT;
    QuiltModDownloadInfo = quiltModDownloadInfo;
  }

  public void setFabricModInstallInfo(FabricDonwloadInfo fabricModInstallInfo) {
    kind = ModUtils.KindOfMod.FABRIC;
    this.FabricModInstallInfo = fabricModInstallInfo;
  }

  public void setKind(ModUtils.KindOfMod kind) {
    this.kind = kind;
  }


  public void setForgeModDownloadInfo(ForgeDownloadInfo forgeModDownloadInfo) {
    kind = ModUtils.KindOfMod.FORGE;
    ForgeModDownloadInfo = forgeModDownloadInfo;
  }

  public GameConfig getGameConfig() {
    return new GameConfig(this);
  }

  public void beforLaunchTask() throws IOException {
    if (SettingManger.getSetting().isChineseLanguage() && FileUtils.isFileNotExists(getGameOptionsFile())) {
      String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/assets/options.txt")));
      FileUtils.writeStringToFile(getGameOptionsFile(), Options);
    }
  }

  public void CleanKind() {
    kind = ModUtils.KindOfMod.Original;
  }

  public Accounts getAccounts() {
    return new Accounts();
  }


  public VersionInfo getVersionInfo() {
    return new VersionInfo(this);
  }

  public GameDirectoryManger getGameDirectoryManger() {
    return this;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Launcher launcher)) return false;
    if (launcher.getVersionNumber().equals(VersionNumber)) {
      return launcher.getKind() == kind;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(VersionNumber, kind);
  }

  @Override
  public String toString() {
    return "Launcher(version=" + VersionNumber + ", kind=" + kind + ")";


  }
}
