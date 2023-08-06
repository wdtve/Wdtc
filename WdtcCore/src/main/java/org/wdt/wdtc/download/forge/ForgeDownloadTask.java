package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.ExtractFile;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;

public class ForgeDownloadTask {
    public static final Logger logmaker = WdtcLogger.getLogger(ForgeDownloadTask.class);
    public final String ForgeVersionNumber;
    public final Launcher launcher;

    public final DownloadSource source;


    public ForgeDownloadTask(Launcher launcher, String forgeVersionNumber) {
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
        DownloadTask.StartWGetDownloadTask(getForgeInstallJarUrl(), getForgeInstallJarPath());
    }

    private String getInstallJarUrl() {
        return source.getForgeLibraryMavenUrl() + "net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    }

    public String getForgeInstallJarPath() {
        return FilePath.getWdtcCache() + "/" + ForgeVersionNumber + "-installer.jar";
    }

    public String getForgeInstallJarUrl() {
        return getInstallJarUrl().replaceAll(":mcversion", launcher.getVersion()).replaceAll(":forgeversion", ForgeVersionNumber);
    }

    public void getInstallProfile() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getForgeInstallJarPath())) {
            DownloadInstallJar();
        }
        ExtractFile.unZipToFile(getForgeInstallJarPath(), getInstallProfilePath(), "install_profile.json");

    }


    public String getInstallProfilePath() {
        return FilePath.getWdtcCache() + "/install_profile" + "-" + launcher.getVersion() + "-" + ForgeVersionNumber + ".json";
    }

    public JSONObject getInstallPrefileJSONObject() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getInstallProfilePath())) {
            getInstallProfile();
        }
        return JSONUtils.getJSONObject(getInstallProfilePath());
    }

    public void DownloadInstallPrefileLibarary() throws IOException {
        DownloadForgeLibraryFile(getInstallProfilePath());
    }

    public void DownloadForgeLibraryFile(String FilePath) throws IOException {
        JSONArray LibraryList = JSONUtils.getJSONObject(FilePath).getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            JSONObject LibraryObject = LibraryList.getJSONObject(i);
            JSONObject LibraryArtifact = LibraryObject.getJSONObject("downloads").getJSONObject("artifact");
            DependencyDownload download = new DependencyDownload(LibraryObject.getString("name"));
            download.setDownloadPath(launcher.GetGameLibraryPath());
            download.setDefaultUrl(source.getFabricLibraryUrl());
            if (PlatformUtils.NetworkHasThisFile(download.getLibraryUrl())) {
                DownloadTask.StartDownloadTask(download.getLibraryUrl(), download.getLibraryFile());
            } else {
                String LibraryUrl = LibraryArtifact.getString("url");
                String LibraryPath = FilenameUtils.separatorsToSystem(launcher.GetGameLibraryPath() + LibraryArtifact.getString("path"));
                DownloadTask.StartDownloadTask(LibraryUrl, LibraryPath);
            }

        }
    }

    public ForgeInstallTask getForgeInstallTask() {
        return new ForgeInstallTask(launcher, ForgeVersionNumber);
    }

    public ForgeLaunchTask getForgeLaunchTask() {
        return new ForgeLaunchTask(launcher, ForgeVersionNumber);
    }

}
