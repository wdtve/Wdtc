package org.wdt.wdtc.download.quilt;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuiltDownloadTask {
    private static final String LibraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s";
    private static final Logger logmaker = getWdtcLogger.getLogger(QuiltDownloadTask.class);
    private final Launcher launcher;
    private final String QuiltVersionNumber;

    public QuiltDownloadTask(Launcher launcher, String quiltVersionNumber) {
        this.launcher = launcher;
        QuiltVersionNumber = quiltVersionNumber;
    }

    public void DownloadQuilt() throws IOException {
        for (Map<String, String> map : LibraryList()) {
            DependencyDownload download = new DependencyDownload(map.get("name"));
            download.setDownloadPath(launcher.GetGameLibraryPath());
            if (launcher.bmclapi() && !map.get("url").equals("https://maven.quiltmc.org/repository/release/")) {
                download.setDefaultUrl(FileUrl.getBmclapiLibraries());
            } else {
                download.setDownloadPath(map.get("url"));
            }
            download.setDefaultUrl(map.get("url"));
            DownloadTask.StartDownloadTask(download.getLibraryUrl(), download.getLibraryFile());
        }

    }

    public List<Map<String, String>> LibraryList() throws IOException {
        List<Map<String, String>> List = new ArrayList<>();
        writeQuiltGameVersionJson();
        JSONObject MetadataObject = Utils.getJSONObject(QuiltGameVersionJson());
        JSONArray LibararyArray = MetadataObject.getJSONObject("launcherMeta").getJSONObject("libraries").getJSONArray("common");
        for (int l = 0; l < LibararyArray.size(); l++) {
            JSONObject LibraryObject = LibararyArray.getJSONObject(l);
            Map<String, String> NameAndUrl = new HashMap<>();
            NameAndUrl.put("name", LibraryObject.getString("name"));
            NameAndUrl.put("url", LibraryObject.getString("url"));
            List.add(NameAndUrl);
        }
        Map<String, String> Loader = new HashMap<>();
        Loader.put("name", MetadataObject.getJSONObject("loader").getString("maven"));
        Loader.put("url", "https://maven.quiltmc.org/repository/release/");
        List.add(Loader);
        Map<String, String> intermediary = new HashMap<>();
        intermediary.put("name", MetadataObject.getJSONObject("intermediary").getString("intermediary"));
        intermediary.put("url", "https://maven.quiltmc.org/repository/release/");
        List.add(intermediary);
        return List;
    }

    public String getQuiltVersionNumber() {
        return QuiltVersionNumber;
    }

    public QuiltLaunchTask getQuiltLaunchTask() {
        return new QuiltLaunchTask(launcher);
    }

    public void writeQuiltGameVersionJson() throws IOException {
        if (PlatformUtils.FileExistenceAndSize(QuiltGameVersionJson())) {
            FileUtils.writeStringToFile(QuiltGameVersionJson(), PlatformUtils.GetUrlContent(String.format(LibraryListUrl, launcher.getVersion(), QuiltVersionNumber)), "UTF-8");
        }
    }

    public File QuiltGameVersionJson() {
        return new File(FilePath.getWdtcCache() + "/" + launcher.getVersion() + "-quilt-" + QuiltVersionNumber + ".json");
    }
}
