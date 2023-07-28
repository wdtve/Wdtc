package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.*;
import java.util.zip.ZipFile;

public class ForgeDownloadTask {
    public static final String INSTALL_JAR = "https://maven.minecraftforge.net/net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    public static final String BMCLAPI_INSTALL_JAR = "https://download.mcbbs.net/maven/net/minecraftforge/forge/:mcversion-:forgeversion/forge-:mcversion-:forgeversion-installer.jar";
    public static final Logger logmaker = getWdtcLogger.getLogger(ForgeDownloadTask.class);
    public final String ForgeVersion;
    public final Launcher launcher;


    public ForgeDownloadTask(Launcher launcher, String forgeVersion) {
        ForgeVersion = forgeVersion;
        this.launcher = launcher;
    }

    public Launcher getLauncher() {
        return launcher;
    }


    public String getForgeVersion() {
        return ForgeVersion;
    }

    public void DownloadInstallJar() {
        DownloadTask.StartWGetDownloadTask(getForgeInstallJarUrl(), getForgeInstallJarPath());
    }

    private String getInstallJarUrl() {
        if (AboutSetting.getSetting().isBmcl()) {
            return BMCLAPI_INSTALL_JAR;
        } else {
            return INSTALL_JAR;
        }
    }

    public String getForgeInstallJarPath() {
        return FilePath.getWdtcCache() + "/" + ForgeVersion + "-installer.jar";
    }

    public String getForgeInstallJarUrl() {
        return getInstallJarUrl().replaceAll(":mcversion", launcher.getVersion()).replaceAll(":forgeversion", ForgeVersion);
    }

    public void getInstallProfile() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getInstallProfilePath())) {
            DownloadInstallJar();
        }
        ZipFile InstallJar = new ZipFile(new File(getForgeInstallJarPath()));
        InputStream inputStream = InstallJar.getInputStream(InstallJar.getEntry("install_profile.json"));
        OutputStream outputStream = new FileOutputStream(getInstallProfilePath());
        IOUtils.copy(inputStream, outputStream);

    }


    public String getInstallProfilePath() {
        return FilePath.getWdtcCache() + "/install_profile" + "-" + launcher.getVersion() + "-" + ForgeVersion + ".json";
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
            if (launcher.bmclapi()) {
                DependencyDownload download = new DependencyDownload(LibraryObject.getString("name"));
                download.setDownloadPath(launcher.GetGameLibraryPath());
                download.setDefaultUrl(FileUrl.getBmclapiLibraries());
                if (PlatformUtils.NetworkHasThisFile(download.getLibraryUrl())) {
                    DownloadTask.StartWGetDownloadTask(download.getLibraryUrl(), download.getLibraryFile());
                } else {
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
