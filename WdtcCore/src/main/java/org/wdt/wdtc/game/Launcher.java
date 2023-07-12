package org.wdt.wdtc.game;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Launcher extends Version {
    private final Logger logmaker = getWdtcLogger.getLogger(getClass());
    private String Gameattribute;
    private String Jvmattribute;
    private String Librartattribute;
    private FabricDownloadTask FabricModDownloadTask;
    private ModList.KindOfMod kind = ModList.KindOfMod.Original;
    private ForgeDownloadTask ForgeModDownloadTask;
    private QuiltDownloadTask QuiltModDownloadTask;

    public Launcher(String version) {
        this(version, AboutSetting.GetDefaultGamePath());
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

    public String getGameattribute() {
        return Gameattribute;
    }

    public void setGameattribute(StringBuilder gameattribute) {
        Gameattribute = new String(gameattribute);
    }

    public String getJvmattribute() {
        return Jvmattribute;
    }

    public void setJvmattribute(StringBuilder jvmattribute) {
        Jvmattribute = new String(jvmattribute);
    }

    public String getLibrartattribute() {
        return Librartattribute;
    }

    public void setLibrartattribute(StringBuilder librartattribute) {
        Librartattribute = new String(librartattribute);
    }

    public boolean bmclapi() {
        return AboutSetting.GetBmclSwitch();
    }

    public boolean log() {
        return AboutSetting.GetLogSwitch();
    }


    public void writeStartScript() {
        try {
            logmaker.info(GetStartScript());
            FileUtils.writeStringToFile(FilePath.getStarterBat(), GetStartScript(), "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetStartScript() {
        return getJvmattribute() + getLibrartattribute() + getGameattribute();
    }

    public Accounts GetAccounts() throws IOException {
        return new Accounts();
    }

    public void LaunchTask() throws IOException {
        if (AboutSetting.GetZHCNSwitch() && PlatformUtils.FileExistenceAndSize(getGameOptionsFile())) {
            String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/options.txt")));
            File OptionsFile = new File(getGameOptionsFile());
            FileUtils.writeStringToFile(OptionsFile, Options, "UTF-8");
        }
    }

    public DownloadVersionGameFile getDownloadVersionGameFile() {
        return new DownloadVersionGameFile(this);
    }

    @Override
    public String toString() {
        return "Launcher{" +
                "version=" + getVersion() +
                ",kind=" + kind +
                '}';
    }
}
