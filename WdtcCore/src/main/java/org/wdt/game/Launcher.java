package org.wdt.game;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.auth.Accounts;
import org.wdt.download.fabric.FabricDownloadTask;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.platform.AboutSetting;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Launcher extends Version {
    private final Logger logmaker = Logger.getLogger(getClass());
    private String Gameattribute;
    private String Jvmattribute;
    private String Librartattribute;
    private FabricDownloadTask FabricModDownloadTask;
    private ModList.KindOfMod kind = ModList.KindOfMod.Original;
    private ForgeDownloadTask ForgeModDownloadTask;

    public Launcher(String version) {
        this(version, AboutSetting.GetDefaultGamePath());
    }

    public Launcher(String version, String here) {
        super(version, here);
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
        Gameattribute = gameattribute.toString();
    }

    public String getJvmattribute() {
        return Jvmattribute;
    }

    public void setJvmattribute(StringBuilder jvmattribute) {
        Jvmattribute = jvmattribute.toString();
    }

    public String getLibrartattribute() {
        return Librartattribute;
    }

    public void setLibrartattribute(StringBuilder librartattribute) {
        Librartattribute = librartattribute.toString();
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

}
