package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.ExtractFile;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForgeLaunchTask extends ForgeDownloadTask {
    private static final Logger logmaker = WdtcLogger.getLogger(ForgeLaunchTask.class);


    public ForgeLaunchTask(Launcher launcher, String forgeVersion) {
        super(launcher, forgeVersion);
    }

    public String getForgeVersionJsonPath() {
        return FilePath.getWdtcCache() + "/version-" + launcher.getVersion() + "-" + ForgeVersionNumber + ".json";
    }

    public void getForgeVersionJson() {
        ExtractFile.unZipToFile(getForgeInstallJarPath(), getForgeVersionJsonPath(), "version.json");
    }

    public JSONObject getForgeVersionJsonObject() {
        try {
            return JSONUtils.getJSONObject(getForgeVersionJsonPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void DownloadVersionJsonLibarary() throws IOException {
        DownloadForgeLibraryFile(getForgeVersionJsonPath());
    }

    public List<String> getForgeLuanchJvm() {
        List<String> list = new ArrayList<>();
        if (getForgeVersionJsonObject().getJSONObject("arguments").has("jvm")) {
            JSONArray JvmList = getForgeVersionJsonObject().getJSONObject("arguments").getJSONArray("jvm");
            for (int i = 0; i < JvmList.size(); i++) {
                list.add(JvmList.getString(i).replace("${library_directory}", launcher.GetGameLibraryPath()).replace("${classpath_separator}", ";").replace("${version_name}", launcher.getVersion()));
            }
        }
        return list;
    }

    public List<String> getForgeLaunchGame() {
        List<String> list = new ArrayList<>();
        JSONArray JvmList = getForgeVersionJsonObject().getJSONObject("arguments").getJSONArray("game");
        for (int i = 0; i < JvmList.size(); i++) {
            list.add(JvmList.getString(i));
        }
        return list;
    }

    public List<String> getForgeLaunchLibrary() {
        List<String> list = new ArrayList<>();
        JSONArray LibraryList = getForgeVersionJsonObject().getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            list.add(FilenameUtils.separatorsToSystem(launcher.GetGameLibraryPath() + LibraryList.getJSONObject(i).getJSONObject("downloads").getJSONObject("artifact").getString("path")));
        }
        return list;
    }

    public String getMainClass() {
        return getForgeVersionJsonObject().getString("mainClass");
    }
}
