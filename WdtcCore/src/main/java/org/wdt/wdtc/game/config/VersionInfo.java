package org.wdt.wdtc.game.config;

import com.google.gson.annotations.SerializedName;
import org.wdt.wdtc.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.ModUtils;

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
        DownloadInfo info = ModUtils.getModDownloadInfo(launcher);
        if (info != null) {
            this.ModVersion = info.getModVersion();
        } else {
            this.ModVersion = null;
        }
    }

    public String getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        VersionNumber = versionNumber;
    }

    public ModUtils.KindOfMod getKind() {
        return kind;
    }

    public void setKind(ModUtils.KindOfMod kind) {
        this.kind = kind;
    }

    public String getModVersion() {
        return ModVersion;
    }

    public void setModVersion(String modVersion) {
        ModVersion = modVersion;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "VersionNumber='" + VersionNumber + '\'' +
                ", kind=" + kind +
                ", ModVersion='" + ModVersion + '\'' +
                '}';
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
