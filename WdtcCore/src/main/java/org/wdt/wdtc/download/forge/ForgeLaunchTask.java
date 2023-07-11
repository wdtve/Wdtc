package org.wdt.wdtc.download.forge;


import org.apache.commons.io.FilenameUtils;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.ExtractFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForgeLaunchTask extends ForgeDownloadTask {

    public ForgeLaunchTask(String mcVersion, String forgeVersion) throws IOException {
        super(mcVersion, forgeVersion);
    }

    public ForgeLaunchTask(Launcher launcher, String forgeVersion) {
        super(launcher, forgeVersion);
    }

    public String getForgeVersionJsonPath() {
        return FilePath.getWdtcCache() + "/version.json";
    }

    public void getForgeVersionJson() {
        ExtractFile.unZipBySpecifyFile(getForgeInstallJarPath(), getForgeVersionJsonPath());
    }

    public JSONObject getForgeVersionJsonObject() throws IOException {
        return Utils.getJSONObject(getForgeVersionJsonPath());
    }

    public void DownloadVersionJsonLibarary() throws IOException {
        DownloadForgeLibraryFile(getForgeVersionJsonPath());
    }

    public List<String> getForgeLuanchJvm() throws IOException {
        List<String> list = new ArrayList<>();
        JSONArray JvmList = getForgeVersionJsonObject().getJSONObject("arguments").getJSONArray("jvm");
        for (int i = 0; i < JvmList.size(); i++) {
            list.add(JvmList.getString(i).replace("${library_directory}", launcher.GetGameLibraryPath())
                    .replace("${classpath_separator}", ";").replace("${version_name}", launcher.getVersion()));
        }
        return list;
    }

    public List<String> getForgeLaunchGame() throws IOException {
        List<String> list = new ArrayList<>();
        JSONArray JvmList = getForgeVersionJsonObject().getJSONObject("arguments").getJSONArray("game");
        for (int i = 0; i < JvmList.size(); i++) {
            list.add(JvmList.getString(i));
        }
        return list;
    }

    public List<String> getForgeLaunchLibrary() throws IOException {
        List<String> list = new ArrayList<>();
        JSONArray LibraryList = getForgeVersionJsonObject().getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            list.add(FilenameUtils.separatorsToSystem(launcher.GetGameLibraryPath() + LibraryList.getJSONObject(i)
                    .getJSONObject("downloads").getJSONObject("artifact").getString("path")));
        }
        return list;
    }
}
