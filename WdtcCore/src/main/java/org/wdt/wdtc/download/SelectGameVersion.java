package org.wdt.wdtc.download;

import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.Objects;

public class SelectGameVersion {
    private static final Logger logmaker = WdtcLogger.getLogger(SelectGameVersion.class);
    private final TextField textField;
    private final Launcher launcher;

    public SelectGameVersion(Launcher launcher, TextField textField) {
        this.textField = textField;
        this.launcher = launcher;
    }

    public SelectGameVersion(Launcher launcher) {
        this(launcher, null);
    }

    public void DownloadGame() {
        try {
            long startTime = System.currentTimeMillis();
            GameConfig config = launcher.getGameConfig();
            if (PlatformUtils.FileExistenceAndSize(config.getVersionConfigFile())) {
                FileUtils.writeStringToFile(config.getVersionConfigFile(), config.getDefaultGameConfig(), "UTF-8");
                logmaker.info(new DefaultGameConfig());
            }
            DownloadVersionGameFile DownloadGame = launcher.getDownloadVersionGameFile();
            DownloadGame.DownloadGameVersionJson();
            DownloadGame.DownloadGameAssetsListJson();
            DownloadGame.DownloadVersionJar();
            ModDonwloadTask.DownloadMod(launcher);
            DownloadGame.DownloadGameLibFileTask().DownloadLibraryFile();
            logmaker.debug("库下载完成");
            DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
            long EndTime = System.currentTimeMillis() - startTime;
            logmaker.info("下载完成,耗时:" + EndTime + "ms");
            if (Objects.nonNull(textField)) {
                textField.setText("下载完成,耗时:" + EndTime + "ms");
            }
        } catch (IOException e) {
            logmaker.error("* Download Game Error,", e);
        }
    }

}





