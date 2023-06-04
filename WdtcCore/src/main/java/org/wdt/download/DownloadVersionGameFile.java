package org.wdt.download;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.wdt.Launcher;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DownloadVersionGameFile extends DownloadTask {
    public final Launcher launcher;
    private final FileUrl fileUrl = new FileUrl();

    public DownloadVersionGameFile(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
    }

    public void DownloadGameVersionJson() throws IOException {
        JSONArray VersionList = JSONObject.parseObject(PlatformUtils.GetUrlContent(fileUrl.getVersionManifest())).getJSONArray("versions");
        for (int i = 0; i < VersionList.size(); i++) {
            String version_name = VersionList.getJSONObject(i).getString("id");
            if (Objects.equals(launcher.getVersion(), version_name)) {
                String VersionJsonUrl = VersionList.getJSONObject(i).getString("url");
                if (launcher.bmclapi()) {
                    VersionJsonUrl = VersionJsonUrl.replaceAll(FileUrl.getPistonMetaMojang(), FileUrl.getBmcalapiCom());
                }
                StartDownloadTask(VersionJsonUrl, launcher.getVersionJson());
                if (launcher.getForgeDownloadTaskIsNull()) {
                    JSONObject VersionJSONObject = PlatformUtils.FileToJSONObject(launcher.getVersionJson());
                    VersionJSONObject.put("id", launcher.getVersion() + "-" + launcher.getForgeDownloadTask().getForgeVersion());
                    PlatformUtils.PutJSONObject(launcher.getVersionJson(), VersionJSONObject);
                }
            }
        }
    }

    public void DownloadGameAssetsListJson() throws IOException {
        JSONObject AssetIndexJson = PlatformUtils.FileToJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex");
        String GameAssetsListJsonUrl = AssetIndexJson.getString("url");
        if (launcher.bmclapi()) {
            GameAssetsListJsonUrl = GameAssetsListJsonUrl.replaceAll(FileUrl.getPistonMetaMojang(), FileUrl.getBmcalapiCom());
        }
        StartDownloadTask(GameAssetsListJsonUrl, launcher.getGameAssetsListJson());
    }

    public void DownloadVersionJar() throws IOException {
        String JarUrl = PlatformUtils.FileToJSONObject(launcher.getVersionJson()).getJSONObject("downloads").getJSONObject("client").getString("url");
        if (launcher.bmclapi()) {
            JarUrl = JarUrl.replaceAll(FileUrl.getPistonDataMojang(), FileUrl.getBmcalapiCom());
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
