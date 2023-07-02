package org.wdt.launch;


import org.apache.commons.io.FilenameUtils;
import org.wdt.download.FileUrl;
import org.wdt.game.Launcher;
import org.wdt.platform.gson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLibraryPathAndUrl {
    private final String BMCLAPI_Libraries = FileUrl.getBmclapiLibraries();
    private final String MOJANG_Libraries = FileUrl.getMojangLibraries();
    private final Launcher launcher;


    public GetLibraryPathAndUrl(Launcher launcher) {
        this.launcher = launcher;
    }

    public File GetNativesLibPath(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibraryPath();
        JSONObject classifiers_j = lib_j.getJSONObject("downloads").getJSONObject("classifiers");
        String natives_name = lib_j.getJSONObject("natives").getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString("path");
        return new File(FilenameUtils.separatorsToWindows(natives_lib_path));
    }

    public URL GetNativesLibUrl(JSONObject lib_j) throws IOException {
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject natives_j = lib_j.getJSONObject("natives");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        String natives_name = natives_j.getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_url = natives_os.getString("url");
        if (launcher.bmclapi()) {
            natives_lib_url = natives_lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(natives_lib_url);
        } else {
            return new URL(natives_lib_url);
        }
    }

    public File GetLibPath(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibraryPath();
        JSONObject artifact_j = lib_j.getJSONObject("downloads").getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path");
        return new File(FilenameUtils.separatorsToWindows(lib_path));

    }

    public URL GetLibUrl(JSONObject lib_j) throws MalformedURLException {
        JSONObject artifact_j = lib_j.getJSONObject("downloads").getJSONObject("artifact");
        String lib_url = artifact_j.getString("url");
        if (launcher.bmclapi()) {
            lib_url = lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(lib_url);
        } else {
            return new URL(lib_url);
        }
    }

}
