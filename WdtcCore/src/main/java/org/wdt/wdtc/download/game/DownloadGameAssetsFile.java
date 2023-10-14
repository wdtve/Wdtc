package org.wdt.wdtc.download.game;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.WdtcLogger;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DownloadGameAssetsFile {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadGameAssetsFileTask.class);
    private final Launcher launcher;

    public DownloadGameAssetsFile(Launcher launcher) {
        this.launcher = launcher;
    }

    @SneakyThrows(IOException.class)
    public void DownloadAssetsFiles() {
        Map<String, JsonElement> list = JSONUtils.getJSONObject(launcher.getGameAssetsListJson())
                .getJSONObject("objects").getJsonObjects().asMap();
        SpeedOfProgress progress = new SpeedOfProgress(list.size());
        for (String key : list.keySet()) {
            AssetsFileData data = JSONUtils.parseObject(list.get(key).getAsJsonObject(), AssetsFileData.class);
            if (DownloadUtils.isDownloadProcess()) return;
            if (FileUtils.isFileNotExistsAndIsNotSameSize(new File(launcher.getGameObjects(), data.getHashSplicing()), data.getSize())) {
                DownloadGameAssetsFileTask task = new DownloadGameAssetsFileTask(launcher, data, progress);
                task.start();
            } else {
                progress.countDown();
            }
        }
        progress.await();
    }

    @EqualsAndHashCode
    @Getter
    public static class AssetsFileData {
        @SerializedName("hash")
        private String hash;
        @SerializedName("size")
        private int size;

        public String getHashSplicing() {
            return getHashHead() + "/" + hash;
        }

        public String getHashHead() {
            return hash.substring(0, 2);
        }
    }

}
