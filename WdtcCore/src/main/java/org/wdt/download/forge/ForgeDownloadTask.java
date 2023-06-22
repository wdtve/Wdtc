package org.wdt.download.forge;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.download.DownloadTask;
import org.wdt.download.FileUrl;
import org.wdt.download.dependency.DependencyDownload;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
        unzipByInstallProfile(getForgeInstallJarPath(), getInstallProfilePath());
    }

    public void unzipByInstallProfile(String file, String path) {
        try {
            File unZipPath = new File(path);
            ZipFile zip = new ZipFile(new File(file));
            for (Enumeration<?> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String name = entry.getName();
                if (name.equals(unZipPath.getName())) {
                    File unfile = new File(FilenameUtils.separatorsToWindows(path));
                    FileUtils.touch(unZipPath);
                    InputStream in = zip.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(unfile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = in.read(buf)) != -1) fos.write(buf, 0, len);
                    fos.close();
                    in.close();
                }
            }
            zip.close();
        } catch (Exception e) {
            logmaker.error("* 压缩包提取发生错误:", e);
        }
    }

    public String getInstallProfilePath() {
        return FilePath.getWdtcCache() + "/install_profile.json";
    }

    public JSONObject getInstallPrefileJSONObject() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(getInstallProfilePath())) {
            getInstallProfile();
        }
        return PlatformUtils.FileToJSONObject(getInstallProfilePath());
    }

    public void DownloadInstallPrefileLibarary() throws IOException {
        DownloadForgeLibraryFile(getInstallProfilePath());
    }

    public void DownloadForgeLibraryFile(String FilePath) throws IOException {
        JSONArray LibraryList = PlatformUtils.FileToJSONObject(FilePath).getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            JSONObject LibraryObject = LibraryList.getJSONObject(i);
            JSONObject LibraryArtifact = LibraryObject.getJSONObject("downloads").getJSONObject("artifact");
            if (launcher.bmclapi()) {
                try {
                    DependencyDownload download = new DependencyDownload(LibraryObject.getString("name"));
                    download.setDefaultUrl(FileUrl.getBmclapiLibraries());
                    download.setPath(launcher.GetGameLibraryPath());
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
}
