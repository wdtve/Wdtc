package org.wdt.wdtc.download.game;


import com.google.gson.JsonElement;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;
import java.util.Map;

public class DownloadGameAssetsFile extends DownloadTask {
    private final Launcher version;

    public DownloadGameAssetsFile(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadResourceFile() throws IOException, InterruptedException {
        Map<String, JsonElement> list = JSONUtils.getJSONObject(version.getGameAssetsListJson()).getJSONObject("objects").getJsonObjects().asMap();
        SpeedOfProgress countDownLatch = new SpeedOfProgress(list.size());
        for (String str : list.keySet()) {
            String hash = JSONObject.parseObject(list.get(str).toString()).getString("hash");
            StartDownloadHashTask(hash, countDownLatch).setName(hash);
        }
        countDownLatch.await();
    }
}
