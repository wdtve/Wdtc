package org.wdt.wdtc.download;

import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.game.DownloadVersionGameFile;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModUtils;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.Objects;

public class InstallGameVersion extends ModUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(InstallGameVersion.class);
    private final TextField textField;
    private final Launcher launcher;
    private final boolean Install;

    private InstallGameVersion(Launcher launcher, TextField textField, boolean Install) {
        this.textField = textField;
        this.launcher = launcher;
        this.Install = Install;
    }

    public InstallGameVersion(Launcher launcher) {
        this(launcher, null, false);
    }

    public InstallGameVersion(Launcher launcher, TextField textField) {
        this(launcher, textField, true);
    }

    public InstallGameVersion(Launcher launcher, boolean Install) {
        this(launcher, null, Install);
    }

    public void DownloadGame() {
        try {
            long startTime = System.currentTimeMillis();
            GameConfig config = launcher.getGameConfig();
            if (PlatformUtils.FileExistenceAndSize(config.getVersionConfigFile())) {
                GameConfig.writeConfigJson(launcher);
            }
            InstallTask task = getModInstallTask(launcher);
            DownloadVersionGameFile DownloadGame = new DownloadVersionGameFile(launcher, Install);
            DownloadGame.DownloadGameVersionJson();
            DownloadGame.DownloadGameAssetsListJson();
            DownloadGame.DownloadVersionJar();
            if (Install && task != null) {
                task.BeforInstallTask();
                task.setPatches();
                task.execute();
            }
            DownloadGame.DownloadGameLibFileTask().DownloadLibraryFile();
            String LibraryFinishTime = "游戏所需类库下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
            logmaker.debug(LibraryFinishTime);
            if (Objects.nonNull(textField)) {
                textField.setText(LibraryFinishTime);
            }
            DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
            if (Install && task != null) {
                task.AfterDownloadTask();
            }
            String EndTime = "下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
            logmaker.info(EndTime);
            if (Objects.nonNull(textField)) {
                textField.setText(EndTime);
            }
        } catch (IOException e) {
            logmaker.error("* Download Game Error,", e);
        }
    }


}





