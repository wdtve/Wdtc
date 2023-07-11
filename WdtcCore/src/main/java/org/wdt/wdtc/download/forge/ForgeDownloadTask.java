package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.ExtractFile;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.PlatformUtils;

import java.io.IOException;

public class ForgeDownloadTask {
    public static final String INSTALL_JAR = "https://maven.minecraftforge.net/net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    public static final String BMCLAPI_INSTALL_JAR = "https://download.mcbbs.net/maven/net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    public final Logger logmaker = Logger.getLogger(getClass());
    public final String McVersion;
    public final String ForgeVersion;
    public final Launcher launcher;

    public ForgeDownloadTask(String mcVersion, String forgeVersion) throws IOException {
        McVersion = mcVersion;
        ForgeVersion = forgeVersion;
        launcher = new Launcher(mcVersion);
    }

    public ForgeDownloadTask(Launcher launcher, String forgeVersion) {
        McVersion = launcher.getVersion();
        ForgeVersion = forgeVersion;
        this.launcher = launcher;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public String getMcVersion() {
        return McVersion;
    }

    public String getForgeVersion() {
        return ForgeVersion;
    }

    public void DownloadInstallJar() {
        DownloadTask.StartWGetDownloadTask(getForgeInstallJarUrl(), getForgeInstallJarPath());
    }

    private String getInstallJar() {
        if (AboutSetting.GetBmclSwitch()) {
            return BMCLAPI_INSTALL_JAR;
        } else {
            return INSTALL_JAR;
        }
    }

    public String getForgeInstallJarPath() {
        return FilePath.getWdtcCache() + "/" + ForgeVersion + "-installer.jar";
    }

    public String getForgeInstallJarUrl() {
        return getInstallJar().replaceAll(":mcversion", McVersion).replaceAll(":forgeversion", ForgeVersion);
    }

    public void getInstallProfile() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getInstallProfilePath())) {
            DownloadInstallJar();
        }
        ExtractFile.unZipBySpecifyFile(getForgeInstallJarPath(), getInstallProfilePath());
    }



    public String getInstallProfilePath() {
        return FilePath.getWdtcCache() + "/install_profile.json";
    }

    public JSONObject getInstallPrefileJSONObject() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getInstallProfilePath())) {
            getInstallProfile();
        }
        return Utils.getJSONObject(getInstallProfilePath());
    }

    public void DownloadInstallPrefileLibarary() throws IOException {
        DownloadForgeLibraryFile(getInstallProfilePath());
    }

    public void DownloadForgeLibraryFile(String FilePath) throws IOException {
        JSONArray LibraryList = Utils.getJSONObject(FilePath).getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            JSONObject LibraryObject = LibraryList.getJSONObject(i);
            JSONObject LibraryArtifact = LibraryObject.getJSONObject("downloads").getJSONObject("artifact");
            if (launcher.bmclapi()) {
                try {
                    DependencyDownload download = new DependencyDownload(LibraryObject.getString("name"));
                    download.setDefaultUrl(FileUrl.getBmclapiLibraries());
                    download.setDownloadPath(launcher.GetGameLibraryPath());
                    download.download();
                } catch (IOException e) {
                    String LibraryUrl = LibraryArtifact.getString("url");
                    String LibraryPath = FilenameUtils.separatorsToSystem(launcher.GetGameLibraryPath() + LibraryArtifact.getString("path"));
                    DownloadTask.StartWGetDownloadTask(LibraryUrl, LibraryPath);
                }
            } else {
                String LibraryUrl = LibraryArtifact.getString("url");
                String LibraryPath = FilenameUtils.separatorsToSystem(launcher.GetGameLibraryPath() + LibraryArtifact.getString("path"));
                DownloadTask.StartWGetDownloadTask(LibraryUrl, LibraryPath);
            }
        }
    }

    public ForgeInstallTask getForgeInstallTask() {
        return new ForgeInstallTask(launcher, ForgeVersion);
    }

    public ForgeLaunchTask getForgeLaunchTask() {
        return new ForgeLaunchTask(launcher, ForgeVersion);
    }
}
