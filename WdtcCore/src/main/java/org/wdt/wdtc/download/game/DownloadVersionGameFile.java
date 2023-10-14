package org.wdt.wdtc.download.game;


import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.manger.UrlManger;
import org.wdt.wdtc.utils.gson.JSONArray;
import org.wdt.wdtc.utils.gson.JSONFileUtils;
import org.wdt.wdtc.utils.gson.JSONObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DownloadVersionGameFile extends DownloadTask {
    public final Launcher launcher;
    public final DownloadSource source;
    public final boolean Install;

    public DownloadVersionGameFile(Launcher launcher, boolean Install) {
        super(launcher);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
        this.Install = Install;
    }

    public static void DownloadVersionManifestJsonFile() {
        DownloadTask.StartDownloadTask(Launcher.getDownloadSource().getVersionManifestUrl(), FileManger.getVersionManifestFile());
        JSONFileUtils.FormatJsonFile(FileManger.getVersionManifestFile());
    }

    public void DownloadGameVersionJson() throws IOException {
        if (FileUtils.isFileNotExists(FileManger.getVersionManifestFile())) {
            DownloadVersionManifestJsonFile();
        }
        JSONArray VersionList = JSONUtils.getJSONObject(FileManger.getVersionManifestFile()).getJSONArray("versions");
        for (int i = 0; i < VersionList.size(); i++) {
            String version_name = VersionList.getJSONObject(i).getString("id");
            if (Objects.equals(launcher.getVersionNumber(), version_name)) {
                String VersionJsonUrl = VersionList.getJSONObject(i).getString("url");
                if (UrlManger.DownloadSourceList.NoOfficialDownloadSource()) {
                    VersionJsonUrl = VersionJsonUrl.replaceAll(UrlManger.getPistonMetaMojang(), source.getMetaUrl());
                }
                if (FileUtils.isFileNotExists(launcher.getVersionJson()) || Install) {
                    StartDownloadTask(VersionJsonUrl, launcher.getVersionJson());

                }
            }
        }
    }

    public void DownloadGameAssetsListJson() throws IOException {
        JSONObject AssetIndexJson = JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex");
        String GameAssetsListJsonUrl = AssetIndexJson.getString("url");
        if (UrlManger.DownloadSourceList.NoOfficialDownloadSource()) {
            GameAssetsListJsonUrl = GameAssetsListJsonUrl.replaceAll(UrlManger.getPistonMetaMojang(), source.getMetaUrl());
        }
        if (FileUtils.isFileNotExistsAndIsNotSameSize(launcher.getGameAssetsListJson(), AssetIndexJson.getInt("size"))) {
            StartDownloadTask(GameAssetsListJsonUrl, launcher.getGameAssetsListJson());
        }
    }

    public void DownloadVersionJar() throws IOException {
        JSONObject ClientObject = JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("downloads")
                .getJSONObject("client");
        String JarUrl = ClientObject.getString("url");
        if (UrlManger.DownloadSourceList.NoOfficialDownloadSource()) {
            JarUrl = JarUrl.replaceAll(UrlManger.getPistonDataMojang(), source.getDataUrl());
        }
        File VersionJar = launcher.getVersionJar();
        if (FileUtils.isFileNotExistsAndIsNotSameSize(VersionJar, ClientObject.getInt("size"))) {
            StartDownloadTask(JarUrl, VersionJar);
        }
    }

    public DownloadGameClass DownloadGameLibraryFileTask() {
        return new DownloadGameClass(launcher);
    }

    public DownloadGameAssetsFile getDownloadGameAssetsFile() {
        return new DownloadGameAssetsFile(launcher);
    }
}
