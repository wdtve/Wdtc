package org.wdt;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.auth.Accounts;
import org.wdt.download.DownloadVersionGameFile;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.download.forge.ForgeInstallTask;
import org.wdt.download.forge.ForgeLaunchTask;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Launcher extends Version {
    private final Logger logmaker = Logger.getLogger(getClass());
    private String Gameattribute;
    private String Jvmattribute;
    private String Librartattribute;
    private ForgeDownloadTask DownloadTask = null;

    public Launcher(String version) throws IOException {
        this(version, AboutSetting.GetDefaultGamePath());
    }

    public Launcher(String version, String here) {
        super(version, here);
    }

    public ForgeDownloadTask getForgeDownloadTask() {
        return DownloadTask;
    }

    public void setDownloadTask(ForgeDownloadTask downloadTask) {
        DownloadTask = downloadTask;
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

    public ForgeLaunchTask getForgeLaunchTask() {
        return new ForgeLaunchTask(DownloadTask.getLauncher(), DownloadTask.getForgeVersion());
    }

    public ForgeInstallTask getForgeInstallTask() {
        return new ForgeInstallTask(DownloadTask.getLauncher(), DownloadTask.getForgeVersion());
    }

    public DownloadVersionGameFile getDownloadVersionGameFile() throws IOException {
        return new DownloadVersionGameFile(new Launcher(getVersion()));
    }

    public boolean getForgeDownloadTaskNoNull() {
        return DownloadTask != null;
    }

    public void LaunchTask() throws IOException {
        if (AboutSetting.GetZHCNSwitch() && PlatformUtils.FileExistenceAndSize(getGameOptionsFile())) {
            String Options = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/options.txt")));
            File OptionsFile = new File(getGameOptionsFile());
            FileUtils.writeStringToFile(OptionsFile, Options, "UTF-8");
        }
    }

}
