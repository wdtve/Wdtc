package org.wdt.wdtc.game;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.download.downloadsource.OfficialDownloadSource;
import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Launcher extends Version {
    private static final Logger logmaker = WdtcLogger.getLogger(Launcher.class);
    private String Gameattribute;
    private String Jvmattribute;
    private String Librartattribute;
    private FabricDownloadTask FabricModDownloadTask;
    private ModList.KindOfMod kind = ModList.KindOfMod.Original;
    private ForgeDownloadTask ForgeModDownloadTask;
    private QuiltDownloadTask QuiltModDownloadTask;

    public Launcher(String version) {
        this(version, AboutSetting.getSetting().getDefaultGamePath());
    }

    public Launcher(String version, String here) {
        super(version, here);
    }

    public QuiltDownloadTask getQuiltModDownloadTask() {
        return QuiltModDownloadTask;
    }

    public void setQuiltModDownloadTask(QuiltDownloadTask quiltModDownloadTask) {
        kind = ModList.KindOfMod.QUILT;
        QuiltModDownloadTask = quiltModDownloadTask;
    }

    public FabricDownloadTask getFabricModDownloadTask() {
        return FabricModDownloadTask;
    }

    public void setFabricModDownloadTask(FabricDownloadTask fabricModDownloadTask) {
        kind = ModList.KindOfMod.FABRIC;
        this.FabricModDownloadTask = fabricModDownloadTask;
    }

    public ModList.KindOfMod getKind() {
        return kind;
    }

    public void setKind(ModList.KindOfMod kind) {
        this.kind = kind;
    }

    public ForgeDownloadTask getForgeDownloadTask() {
        return ForgeModDownloadTask;
    }

    public void setForgeModDownloadTask(ForgeDownloadTask forgeModDownloadTask) {
        kind = ModList.KindOfMod.FORGE;
        ForgeModDownloadTask = forgeModDownloadTask;
    }


    public void setGameattribute(StringBuilder gameattribute) {
        Gameattribute = new String(gameattribute);
    }


    public void setJvmattribute(StringBuilder jvmattribute) {
        Jvmattribute = new String(jvmattribute);
    }


    public void setLibrartattribute(StringBuilder librartattribute) {
        Librartattribute = new String(librartattribute);
    }

    public static DownloadSource getDownloadSource() {
        return FileUrl.DownloadSourceList.getDownloadSource();
    }


    public void writeStartScript() {
        try {
            String Script = GetStartScript();
            logmaker.info(Script);
            FileUtils.writeStringToFile(FilePath.getStarterBat(), Script, "UTF-8", false);
        } catch (IOException e) {
            logmaker.error("* Error,", e);
        }
    }

    public String GetStartScript() {
        return Jvmattribute + Librartattribute + Gameattribute;
    }

    public static DownloadSource getOfficialDownloadSource() {
        return new OfficialDownloadSource();
    }

    public void LaunchTask() throws IOException {
        if (AboutSetting.getSetting().isChineseLanguage() && PlatformUtils.FileExistenceAndSize(getGameOptionsFile())) {
            String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/options.txt")));
            File OptionsFile = new File(getGameOptionsFile());
            FileUtils.writeStringToFile(OptionsFile, Options, "UTF-8");
        }
    }

    public DownloadVersionGameFile getDownloadVersionGameFile() {
        return new DownloadVersionGameFile(this);
    }

    public GameConfig getGameConfig() {
        return new GameConfig(this);
    }

    public GetGamePath getGetGamePath() {
        return new GetGamePath(getHere());
    }

    public void CleanKind() {
        kind = ModList.KindOfMod.Original;
    }

    public boolean Console() {
        return AboutSetting.getSetting().isConsole();
    }

    public Accounts GetAccounts() {
        return new Accounts();
    }

    public FileUrl.DownloadSourceList getDownloadSourceKind() {
        return AboutSetting.getSetting().getDownloadSource();
    }


    @Override
    public String toString() {
        return "Launcher{" +
                "version=" + getVersion() +
                ",kind=" + kind +
                '}';
    }
}
