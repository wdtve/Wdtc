package org.wdt.wdtc.download.game;


import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DownloadGameClass extends DownloadTask {
    private final Logger logmaker = WdtcLogger.getLogger(DownloadGameClass.class);
    private final Launcher launcher;

    public DownloadGameClass(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
    }

    public void DownloadLibraryFile() {
        try {
            Files.createDirectories(launcher.getVersionNativesPath().toPath());
            List<GetGameNeedLibraryFile.LibraryFile> FileList = new GetGameNeedLibraryFile(launcher).getFileList();
            SpeedOfProgress speed = new SpeedOfProgress(FileList.size());
            for (GetGameNeedLibraryFile.LibraryFile libraryFile : FileList) {
                if (libraryFile.isNativesLibrary()) {
                    StartDownloadNativesLibTask(libraryFile.getLibraryObject(), speed);
                } else {
                    StartDownloadLibraryTask(libraryFile.getLibraryObject(), speed);
                }
            }
            speed.await();
        } catch (IOException e) {
            logmaker.error("* Download Library File Error,", e);
        }
        try {
            File VersionLog = launcher.getVersionLog4j2();
            FileUtils.writeStringToFile(VersionLog, IOUtils.toString(getClass().getResourceAsStream("/log4j2.xml")));

        } catch (IOException e) {
            logmaker.error("* logej.xml不存在或路径错误!", e);
        }
    }
}


