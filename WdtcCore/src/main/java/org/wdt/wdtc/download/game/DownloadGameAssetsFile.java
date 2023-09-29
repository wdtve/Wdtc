package org.wdt.wdtc.download.game;


import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.JSONObject;
import org.wdt.utils.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.Map;

public class DownloadGameAssetsFile extends DownloadTask {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadGameAssetsFile.class);
    private final Launcher launcher;

    public DownloadGameAssetsFile(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
    }

    public void DownloadResourceFile() {
        try {
            Map<String, JsonElement> list = JSONUtils.getJSONObject(launcher.getGameAssetsListJson()).getJSONObject("objects").getJsonObjects().asMap();
            SpeedOfProgress countDownLatch = new SpeedOfProgress(list.size());
            ThreadGroup group = new ThreadGroup("Download Assets");
            for (String str : list.keySet()) {
                if (DownloadUtils.isDownloadProcess()) {
                    return;
                }
                JSONObject object = new JSONObject(list.get(str).getAsJsonObject());
                String hash = object.getString("hash");
                int FileSize = object.getInt("size");
                StartDownloadHashTask(hash, FileSize, countDownLatch, group);
            }
        } catch (IOException exception) {
            logmaker.error("Download Assets File Error,", exception);
        }
    }
}
