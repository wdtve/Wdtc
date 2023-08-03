package org.wdt.wdtc.download.game;


import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.Map;

public class DownloadGameAssetsFile extends DownloadTask {
    private final Logger logmaker = WdtcLogger.getLogger(DownloadGameAssetsFile.class);
    private final Launcher version;

    public DownloadGameAssetsFile(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadResourceFile() {
        try {
            Map<String, JsonElement> list = JSONUtils.getJSONObject(version.getGameAssetsListJson()).getJSONObject("objects").getJsonObjects().asMap();
            SpeedOfProgress countDownLatch = new SpeedOfProgress(list.size());
            for (String str : list.keySet()) {
                String hash = list.get(str).getAsJsonObject().get("hash").getAsString();
                StartDownloadHashTask(hash, countDownLatch).setName(hash);
            }
            countDownLatch.await();
        } catch (IOException exception) {
            logmaker.error("* Download Assets File Error,", exception);
        }
    }
}
