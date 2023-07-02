package org.wdt.download.fabric;


import org.apache.commons.io.FilenameUtils;
import org.wdt.download.DownloadTask;
import org.wdt.game.FilePath;
import org.wdt.game.Launcher;
import org.wdt.launch.ExtractFile;
import org.wdt.platform.AboutSetting;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FabricFileList {
    private final String FabricFileListUrl = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private final String BmclapiFabricFileListUrl = "https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader/%s/%s";
    private final String FabricVersionNumber;
    private final Launcher launcher;

    public FabricFileList(String FabricVersionNumber, String GameVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = new Launcher(GameVersionNumber);
    }

    public FabricFileList(String FabricVersionNumber, Launcher launcher) {
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
        if (AboutSetting.GetBmclSwitch()) {
            return getBmclapiFabricFileListUrl();
        } else {
            return getFabricFileListUrl();
        }
    }

    public List<String> getFabricFileName() throws IOException {
        List<String> StringFileList = new ArrayList<>();
        JSONObject FileList = JSONObject.parseObject(PlatformUtils.GetUrlContent(String.format(getFabricFileList(), launcher.getVersion(), FabricVersionNumber)));
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

    public FabricDownloadTask getFabricDownloadTask() {
        return new FabricDownloadTask(FabricVersionNumber, launcher);
    }

    public FabricLaunchTask getFabricLaunchTask() {
        return new FabricLaunchTask(FabricVersionNumber, launcher);
    }
}
