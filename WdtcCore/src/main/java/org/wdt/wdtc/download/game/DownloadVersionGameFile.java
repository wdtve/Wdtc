package org.wdt.wdtc.download.game;


import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DownloadVersionGameFile extends DownloadTask {
    public final Launcher launcher;
    public final DownloadSource source;

    public DownloadVersionGameFile(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public void DownloadGameVersionJson() throws IOException {
        JSONArray VersionList = JSONObject.parseWdtObject(PlatformUtils.GetUrlContent(source.getVersionManifestUrl())).getJSONArray("versions");
        for (int i = 0; i < VersionList.size(); i++) {
            String version_name = VersionList.getJSONObject(i).getString("id");
            if (Objects.equals(launcher.getVersion(), version_name)) {
                String VersionJsonUrl = VersionList.getJSONObject(i).getString("url");
                if (FileUrl.DownloadSourceList.NoOfficialDownloadSource()) {
                    VersionJsonUrl = VersionJsonUrl.replaceAll(FileUrl.getPistonMetaMojang(), source.getMetaUrl());
                }
                StartDownloadTask(VersionJsonUrl, launcher.getVersionJson());
                ModList.putGameId(launcher);
            }
        }
    }

    public void DownloadGameAssetsListJson() throws IOException {
        JSONObject AssetIndexJson = JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex");
        String GameAssetsListJsonUrl = AssetIndexJson.getString("url");
        if (FileUrl.DownloadSourceList.NoOfficialDownloadSource()) {
            GameAssetsListJsonUrl = GameAssetsListJsonUrl.replaceAll(FileUrl.getPistonMetaMojang(), source.getMetaUrl());
        }
        StartDownloadTask(GameAssetsListJsonUrl, launcher.getGameAssetsListJson());
    }

    public void DownloadVersionJar() throws IOException {
        String JarUrl = JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("downloads").getJSONObject("client").getString("url");
        if (FileUrl.DownloadSourceList.NoOfficialDownloadSource()) {
            JarUrl = JarUrl.replaceAll(FileUrl.getPistonDataMojang(), source.getDataUrl());
        }
        File VersionJar = new File(launcher.getVersionJar());
        StartDownloadTask(JarUrl, VersionJar);
    }

    public DownloadGameLibrary DownloadGameLibFileTask() {
        return new DownloadGameLibrary(launcher);
    }

    public DownloadGameAssetsFile DownloadResourceFileTask() {
        return new DownloadGameAssetsFile(launcher);
    }
}
