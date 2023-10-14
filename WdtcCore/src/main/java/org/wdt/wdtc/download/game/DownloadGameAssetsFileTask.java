package org.wdt.wdtc.download.game;


import lombok.SneakyThrows;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadGameAssetsFileTask extends Thread {
    private final Launcher launcher;
    private final DownloadGameAssetsFile.AssetsFileData data;
    private final SpeedOfProgress progress;
    private final DownloadSource source;

    public DownloadGameAssetsFileTask(Launcher launcher, DownloadGameAssetsFile.AssetsFileData data, SpeedOfProgress progress) {
        this.launcher = launcher;
        this.data = data;
        this.progress = progress;
        this.source = Launcher.getDownloadSource();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        File HashFile = new File(launcher.getGameObjects(), data.getHashSplicing());
        URL HashUrl = new URL(source.getAssetsUrl() + data.getHashSplicing());
        DownloadTask.StartDownloadTask(HashUrl, HashFile);
        progress.countDown();
    }
}
