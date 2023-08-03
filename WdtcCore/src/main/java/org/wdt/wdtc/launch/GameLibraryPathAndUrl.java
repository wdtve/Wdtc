package org.wdt.wdtc.launch;


import org.apache.commons.io.FilenameUtils;
import org.wdt.platform.DependencyDownload;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GameLibraryPathAndUrl {
    private static final String MOJANG_Libraries = FileUrl.getMojangLibraries();
    private final Launcher launcher;
    private final DownloadSource source;


    public GameLibraryPathAndUrl(Launcher launcher) {
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
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
        if (FileUrl.DownloadSourceList.NoOfficialDownloadSource()) {
            natives_lib_url = natives_lib_url.replaceAll(MOJANG_Libraries, source.getLibraryUrl());
        }
        return new URL(natives_lib_url);
    }

    public File GetLibPath(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibraryPath();
        DependencyDownload dependency = new DependencyDownload(lib_j.getString("name"));
        dependency.setDownloadPath(game_lib_path);
        return dependency.getLibraryFile();

    }

    public URL GetLibUrl(JSONObject lib_j) throws MalformedURLException {
        DependencyDownload dependency = new DependencyDownload(lib_j.getString("name"));
        dependency.setDefaultUrl(source.getLibraryUrl());
        return dependency.getLibraryUrl();
    }

}
