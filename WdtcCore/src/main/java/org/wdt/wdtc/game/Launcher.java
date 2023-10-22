package org.wdt.wdtc.game;


import lombok.Getter;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.auth.accounts.Accounts;
import org.wdt.wdtc.download.downloadsource.OfficialDownloadSource;
import org.wdt.wdtc.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.quilt.QuiltDownloadInfo;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.game.config.VersionInfo;
import org.wdt.wdtc.manger.GameFileManger;
import org.wdt.wdtc.manger.GameFolderManger;
import org.wdt.wdtc.manger.SettingManger;
import org.wdt.wdtc.manger.URLManger;
import org.wdt.wdtc.utils.ModUtils;

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

    public ForgeDownloadInfo getForgeDownloadInfo() {
        return ForgeModDownloadInfo;
    }

    public void setForgeModDownloadInfo(ForgeDownloadInfo forgeModDownloadInfo) {
        kind = ModUtils.KindOfMod.FORGE;
        ForgeModDownloadInfo = forgeModDownloadInfo;
    }


    public static DownloadSource getDownloadSource() {
        return URLManger.DownloadSourceList.getDownloadSource();
    }

    public static Launcher getPreferredLauncher() {
        SettingManger.Setting setting = SettingManger.getSetting();
        return setting.getPreferredVersion() != null
                ? ModUtils.getModTask(new Launcher(setting.getPreferredVersion()))
                : null;
    }

    public static DownloadSource getOfficialDownloadSource() {
        return new OfficialDownloadSource();
    }


    public GameConfig getGameConfig() {
        return new GameConfig(this);
    }

    public void LaunchTask() throws IOException {
        if (SettingManger.getSetting().isChineseLanguage() && FileUtils.isFileNotExists(getGameOptionsFile())) {
            String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/options.txt")));
            FileUtils.writeStringToFile(getGameOptionsFile(), Options);
        }
    }

    public void CleanKind() {
        kind = ModUtils.KindOfMod.Original;
    }

    public boolean Console() {
        return SettingManger.getSetting().isConsole();
    }

    public Accounts getAccounts() {
        return new Accounts();
    }

    public URLManger.DownloadSourceList getDownloadSourceKind() {
        return SettingManger.getSetting().getDownloadSource();
    }

    public VersionInfo getVersionInfo() {
        return new VersionInfo(this);
    }

    public GameFolderManger getGetGamePath() {
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
    public String toString() {
        return "Launcher{" +
                "version=" + VersionNumber +
                ",kind=" + kind +
                '}';


    }
}
