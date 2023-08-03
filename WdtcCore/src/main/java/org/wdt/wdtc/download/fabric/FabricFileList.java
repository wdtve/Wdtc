package org.wdt.wdtc.download.fabric;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricFileList {
    private final String FabricVersionNumber;
    private final Launcher launcher;
    private static final Logger logmaker = WdtcLogger.getLogger(FabricFileList.class);
    private final DownloadSource source;


    public FabricFileList(Launcher launcher, String FabricVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public String getFabricVersionNumber() {
        return FabricVersionNumber;
    }


    public String getFabricFileList() {
        return source.getFabricMetaUrl() + "v2/versions/loader/%s/%s";
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

    public void DownloadProfileZip() {
        DownloadTask.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
    }

    public String getProfileZipFile() {
        return String.format(FilePath.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String getProfileZipUrl() {
        return String.format(source.getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/zip", launcher.getVersion(), getFabricVersionNumber());
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
}
