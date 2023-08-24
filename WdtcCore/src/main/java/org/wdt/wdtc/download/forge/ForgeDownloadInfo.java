package org.wdt.wdtc.download.forge;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;
import org.wdt.wdtc.utils.ZipUtils;

import java.io.IOException;

public class ForgeDownloadInfo implements DownloadInfo {
    private static final Logger logmaker = WdtcLogger.getLogger(ForgeDownloadInfo.class);
    protected final String ForgeVersionNumber;
    protected final Launcher launcher;

    public final DownloadSource source;


    public ForgeDownloadInfo(Launcher launcher, String forgeVersionNumber) {
        ForgeVersionNumber = forgeVersionNumber;
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public Launcher getLauncher() {
        return launcher;
    }


    public String getForgeVersionNumber() {
        return ForgeVersionNumber;
    }

    public void DownloadInstallJar() {
        DownloadTask.StartDownloadTask(getForgeInstallJarUrl(), getForgeInstallJarPath());
    }

    private String getInstallJarUrl() {
        return source.getForgeLibraryMavenUrl() + "net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    }

    public String getForgeInstallJarPath() {
        return FilePath.getWdtcCache() + "/" + ForgeVersionNumber + "-installer.jar";
    }

    public String getForgeInstallJarUrl() {
        return getInstallJarUrl().replaceAll(":mcversion", launcher.getVersionNumber()).replaceAll(":forgeversion", ForgeVersionNumber);
    }

    public void getInstallProfile() {
        ZipUtils.unZipToFile(getForgeInstallJarPath(), getInstallProfilePath(), "install_profile.json");

    }


    public String getInstallProfilePath() {
        return FilePath.getWdtcCache() + "/install_profile" + "-" + launcher.getVersionNumber() + "-" + ForgeVersionNumber + ".json";
    }

    public JSONObject getInstallPrefileJSONObject() throws IOException {
        return JSONUtils.getJSONObject(getInstallProfilePath());
    }


    public String getForgeVersionJsonPath() {
        return FilePath.getWdtcCache() + "/version-" + launcher.getVersionNumber() + "-" + ForgeVersionNumber + ".json";
    }

    public void getForgeVersionJson() {
        ZipUtils.unZipToFile(getForgeInstallJarPath(), getForgeVersionJsonPath(), "version.json");
    }

    public JSONObject getForgeVersionJsonObject() {
        try {
            return JSONUtils.getJSONObject(getForgeVersionJsonPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getModVersion() {
        return ForgeVersionNumber;
    }

    @Override
    public InstallTask getModInstallTask() {
        return new ForgeInstallTask(launcher, ForgeVersionNumber);
    }
}
