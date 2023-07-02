package org.wdt.download;


import org.apache.log4j.Logger;
import org.wdt.game.Launcher;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.gson.JSONArray;

import java.io.IOException;

public class DownloadGameAssetsFile extends DownloadTask {
    private final Launcher version;
    private final Logger logger = Logger.getLogger(getClass());

    public DownloadGameAssetsFile(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadResourceFile() throws IOException, InterruptedException {
        String list = PlatformUtils.FileToJSONObject(version.getGameAssetsListJson()).getJSONObject("objects").values().toString();
        JSONArray ListArray = JSONArray.parseWdtArray(list);
        SpeedOfProgress countDownLatch = new SpeedOfProgress(ListArray.size());
        for (int i = 0; i < ListArray.size(); i++) {
            String hash = ListArray.getJSONObject(i).getString("hash");
            StartDownloadHashTask(hash, countDownLatch).start();
        }
        countDownLatch.await();
    }
}
