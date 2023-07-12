package org.wdt.wdtc.download;

import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.IOException;
import java.util.Objects;

public class SelectGameVersion {
    private final Logger logmaker = getWdtcLogger.getLogger(SelectGameVersion.class);
    private final TextField label;
    private final Launcher launcher;

    public SelectGameVersion(Launcher launcher, TextField label) {
        this.label = label;
        this.launcher = launcher;
    }

    public SelectGameVersion(Launcher launcher) {
        this(launcher, null);
    }

    public void DownloadGame() throws IOException, InterruptedException {
        DownloadVersionGameFile DownloadGame = launcher.getDownloadVersionGameFile();
        DownloadGame.DownloadGameVersionJson();
        DownloadGame.DownloadGameAssetsListJson();
        DownloadGame.DownloadVersionJar();
        ModDonwloadTask.DownloadMod(launcher);
        DownloadGame.DownloadGameLibFileTask().DownloadLibraryFile();
        logmaker.debug("库下载完成");
        DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
        logmaker.info("下载完成");
        if (Objects.nonNull(label)) {
            label.setText("下载完成");
        }
    }

}





