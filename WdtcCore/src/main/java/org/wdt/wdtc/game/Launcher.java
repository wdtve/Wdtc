package org.wdt.wdtc.game;


import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.download.downloadsource.OfficialDownloadSource;
import org.wdt.wdtc.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.quilt.QuiltDownloadInfo;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.game.config.VersionInfo;
import org.wdt.wdtc.launch.GamePath;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.SettingManger;
import org.wdt.wdtc.manger.UrlManger;
import org.wdt.wdtc.utils.ModUtils;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Launcher extends Version {
    private static final Logger logmaker = WdtcLogger.getLogger(Launcher.class);
    private String Gameattribute;
    private String Jvmattribute;
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

    public QuiltDownloadInfo getQuiltModDownloadInfo() {
        return QuiltModDownloadInfo;
    }

    public void setQuiltModDownloadInfo(QuiltInstallTask quiltModDownloadInfo) {
        kind = ModUtils.KindOfMod.QUILT;
        QuiltModDownloadInfo = quiltModDownloadInfo;
    }

    public FabricDonwloadInfo getFabricModInstallInfo() {
        return FabricModInstallInfo;
    }

    public void setFabricModInstallInfo(FabricDonwloadInfo fabricModInstallInfo) {
        kind = ModUtils.KindOfMod.FABRIC;
        this.FabricModInstallInfo = fabricModInstallInfo;
    }

    public ModUtils.KindOfMod getKind() {
        return kind;
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


    public void setGameattribute(StringBuilder gameattribute) {
        Gameattribute = new String(gameattribute);
    }


    public void setJvmattribute(StringBuilder jvmattribute) {
        Jvmattribute = new String(jvmattribute);
    }


    public static DownloadSource getDownloadSource() {
        return UrlManger.DownloadSourceList.getDownloadSource();
    }

    public static Launcher getPreferredLauncher() {
        SettingManger.Setting setting = SettingManger.getSetting();
        if (setting.getPreferredVersion() != null) {
            return ModUtils.getModTask(new Launcher(setting.getPreferredVersion()));
        } else {
            return null;
        }
    }

    public static DownloadSource getOfficialDownloadSource() {
        return new OfficialDownloadSource();
    }

    public void writeStartScript() {
        try {
            String Script = Jvmattribute + Gameattribute;
            logmaker.info(Script);
            FileUtils.writeStringToFile(FileManger.getStarterBat(), Script);
        } catch (IOException e) {
            logmaker.error("* Error,", e);
        }
    }

    public GameConfig getGameConfig() {
        return new GameConfig(this);
    }

    public void LaunchTask() throws IOException {
        if (SettingManger.getSetting().isChineseLanguage() && PlatformUtils.FileExistenceAndSize(getGameOptionsFile())) {
            String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/options.txt")));
            File OptionsFile = getGameOptionsFile();
            FileUtils.writeStringToFile(OptionsFile, Options);
        }
    }

    public void CleanKind() {
        kind = ModUtils.KindOfMod.Original;
    }

    public boolean Console() {
        return SettingManger.getSetting().isConsole();
    }

    public Accounts GetAccounts() {
        return new Accounts();
    }

    public UrlManger.DownloadSourceList getDownloadSourceKind() {
        return SettingManger.getSetting().getDownloadSource();
    }

    public VersionInfo getVersionInfo() {
        return new VersionInfo(this);
    }

    public GamePath getGetGamePath() {
        return new GamePath(getHere());
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
