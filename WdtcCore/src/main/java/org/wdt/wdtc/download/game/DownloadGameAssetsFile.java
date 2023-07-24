package org.wdt.wdtc.download.game;


import com.alibaba.fastjson2.JSONArray;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.Timer;

public class DownloadGameAssetsFile extends DownloadTask {
    private final Launcher version;

    public DownloadGameAssetsFile(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadResourceFile() throws IOException, InterruptedException {
        JSONArray ListArray = new JSONArray(PlatformUtils.FileToJSONObject(version.getGameAssetsListJson()).getJSONObject("objects").values());
        SpeedOfProgress countDownLatch = new SpeedOfProgress((double) ListArray.size());
        Timer timer = new Timer();
        for (int i = 0; i < ListArray.size(); i++) {
            String hash = ListArray.getJSONObject(i).getString("hash");
            StartDownloadHashTask(hash, countDownLatch).setName(hash);
        }
        timer.cancel();
        countDownLatch.await();
    }
}
