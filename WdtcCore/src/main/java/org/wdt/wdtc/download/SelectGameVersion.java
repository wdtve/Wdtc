package org.wdt.wdtc.download;

import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

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
        GameConfig config = launcher.getGameConfig();
        DownloadVersionGameFile DownloadGame = launcher.getDownloadVersionGameFile();
        DownloadGame.DownloadGameVersionJson();
        DownloadGame.DownloadGameAssetsListJson();
        DownloadGame.DownloadVersionJar();
        ModDonwloadTask.DownloadMod(launcher);
        DownloadGame.DownloadGameLibFileTask().DownloadLibraryFile();
        logmaker.debug("库下载完成");
        DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
        logmaker.info("下载完成");
        if (PlatformUtils.FileExistenceAndSize(config.getVersionConfigFile())) {
            FileUtils.writeStringToFile(config.getVersionConfigFile(), JSONObject.toJSONString(new DefaultGameConfig()), "UTF-8");
        }
        logmaker.info(new DefaultGameConfig());
        if (Objects.nonNull(label)) {
            label.setText("下载完成");
        }
    }

}





