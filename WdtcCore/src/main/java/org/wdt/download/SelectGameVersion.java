package org.wdt.download;

import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.wdt.Launcher;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.download.forge.ForgeInstallTask;
import org.wdt.download.forge.ForgeLaunchTask;

import java.io.IOException;
import java.util.Objects;

public class SelectGameVersion {
    private final Logger LOGGER = Logger.getLogger(SelectGameVersion.class);
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
        DownloadVersionGameFile DownloadGame = new DownloadVersionGameFile(launcher);
        DownloadGame.DownloadGameVersionJson();
        DownloadGame.DownloadGameAssetsListJson();
        DownloadGame.DownloadVersionJar();
        if (launcher.getForgeDownloadTaskNoNull()) {
            DownloadForge();
        }
        DownloadGame.DownloadGameLibFileTask().DownloadLibraryFile();
        LOGGER.debug("库下载完成");
        DownloadGame.DownloadResourceFileTask().DownloadResourceFile();
        LOGGER.info("下载完成");
        if (Objects.nonNull(label)) {
            label.setText("下载完成");
        }
    }

    private void DownloadForge() throws IOException {
        ForgeDownloadTask download = launcher.getForgeDownloadTask();
        ForgeInstallTask install = launcher.getForgeInstallTask();
        ForgeLaunchTask forgeLaunchTask = launcher.getForgeLaunchTask();
        download.DownloadInstallJar();
        download.getInstallProfile();
        download.DownloadInstallPrefileLibarary();
        install.DownloadClientText();
        install.InstallForge();
        forgeLaunchTask.getForgeVersionJson();
        forgeLaunchTask.DownloadVersionJsonLibarary();
    }
}





