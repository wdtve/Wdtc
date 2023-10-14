package org.wdt.wdtc.download;

import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.GameConfig;
import org.wdt.wdtc.utils.ModUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.Objects;

public class InstallGameVersion extends DownloadGameVersion {
    private static final Logger logmaker = WdtcLogger.getLogger(InstallGameVersion.class);
    private final TextField textField;

    private InstallGameVersion(Launcher launcher, TextField textField, boolean Install) {
        super(launcher, Install);
        this.textField = textField;
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

    public void InstallGame() {
        try {
            long startTime = System.currentTimeMillis();
            if (FileUtils.isFileNotExists(launcher.getVersionConfigFile())) {
                GameConfig.writeConfigJson(launcher);
            } else {
                GameConfig.CkeckVersionInfo(launcher);
            }
            DownloadGameFile();
            InstallTask task = ModUtils.getModInstallTask(launcher);
            if (install && task != null) {
                task.BeforInstallTask();
                task.setPatches();
                task.execute();
            }
            DownloadGameLibrary();
            String LibraryFinishTime = "游戏所需类库下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
            logmaker.info(LibraryFinishTime);
            if (Objects.nonNull(textField)) {
                textField.setText(LibraryFinishTime);
            }
            if (install && task != null) {
                task.AfterDownloadTask();
            }
            DownloadResourceFile();
            String EndTime = "下载完成,耗时:" + (System.currentTimeMillis() - startTime) + "ms";
            logmaker.info(EndTime);
            if (Objects.nonNull(textField)) {
                textField.setText(EndTime);
            }
        } catch (IOException e) {
            logmaker.error("Download Game Error,", e);
        }
    }
}





