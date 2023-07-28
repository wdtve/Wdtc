package org.wdt.wdtc.download.fabric;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.ExtractFile;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FabricFileList {
    private final String FabricFileListUrl = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private final String BmclapiFabricFileListUrl = "https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader/%s/%s";
    private final String FabricVersionNumber;
    private final Launcher launcher;
    private final Logger logmaker = getWdtcLogger.getLogger(FabricFileList.class);


    public FabricFileList(Launcher launcher, String FabricVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = launcher;
    }

    public String getFabricVersionNumber() {
        return FabricVersionNumber;
    }


    public String getFabricFileListUrl() {
        return FabricFileListUrl;
    }

    public String getBmclapiFabricFileListUrl() {
        return BmclapiFabricFileListUrl;
    }

    public String getFabricFileList() {
        if (AboutSetting.getSetting().isBmcl()) {
            return getBmclapiFabricFileListUrl();
        } else {
            return getFabricFileListUrl();
        }
    }

    public List<String> getFabricFileName() throws IOException {
        writeCacheVersionJson();
        List<String> StringFileList = new ArrayList<>();
        JSONObject FileList = JSONUtils.getJSONObject(getCacheVersionJson());
        JSONObject loader = FileList.getJSONObject("loader");
        StringFileList.add(loader.getString("maven"));
        JSONObject intermediary = FileList.getJSONObject("intermediary");
        StringFileList.add(intermediary.getString("maven"));
        JSONArray common = FileList.getJSONObject("launcherMeta").getJSONObject("libraries").getJSONArray("common");
        for (int i = 0; i < common.size(); i++) {
            JSONObject FabricList = common.getJSONObject(i);
            StringFileList.add(FabricList.getString("name"));
        }
        return StringFileList;
    }

    public void DownloadProfileZip() throws MalformedURLException {
        DownloadTask.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
    }

    public String getProfileZipFile() {
        return String.format(FilePath.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String getProfileZipUrl() {
        return String.format("https://meta.fabricmc.net/v2/versions/loader/%s/%s/profile/zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public void unJsonAndJarFormZip() {
        ExtractFile.unZipBySpecifyFile(getProfileZipFile(), getFrofileJson());
    }

    public String FromFabricLoaderFolder() {
        return String.format("fabric-loader-%s-%s", getFabricVersionNumber(), launcher.getVersion());
    }

    public String getFrofileJson() {
        return FilenameUtils.separatorsToWindows(FilePath.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".json");
    }

    public String getFabricJar() {
        return FilenameUtils.separatorsToWindows(FilePath.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".jar");
    }

    public File getCacheVersionJson() {
        return new File(String.format(FilePath.getWdtcCache() + "/%s-fabric-%s.json", launcher.getVersion(), FabricVersionNumber));
    }

    public void writeCacheVersionJson() {
        try {
            FileUtils.writeStringToFile(getCacheVersionJson(), PlatformUtils.GetUrlContent(String.format(getFabricFileList(), launcher.getVersion(), FabricVersionNumber)), "UTF-8");
        } catch (IOException e) {
            logmaker.error("Error", e);
        }
    }

    public FabricDownloadTask getFabricDownloadTask() {
        return new FabricDownloadTask(launcher, FabricVersionNumber);
    }

    public FabricLaunchTask getFabricLaunchTask() {
        return new FabricLaunchTask(launcher, FabricVersionNumber);
    }

    @Override
    public String toString() {
        return "FabricFileList{" + "FabricFileListUrl='" + FabricFileListUrl + '\'' + ", BmclapiFabricFileListUrl='" + BmclapiFabricFileListUrl + '\'' + ", FabricVersionNumber='" + FabricVersionNumber + '\'' + '}';
    }
}
