package org.wdt.wdtc.download.game;


import org.apache.log4j.Logger;
import org.wdt.utils.FileUtils;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DownloadGameLibrary extends DownloadTask {
    private final Logger logmaker = WdtcLogger.getLogger(DownloadGameLibrary.class);
    private final Launcher launcher;

    public DownloadGameLibrary(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
    }

    public void DownloadLibraryFile() {
        try {
            Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
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
            File VersionLog = new File(launcher.getVersionLog4j2());
            FileUtils.writeStringToFile(VersionLog, IOUtils.toString(getClass().getResourceAsStream("/log4j2.xml")));

        } catch (IOException e) {
            logmaker.error("* logej.xml不存在或路径错误!", e);
        }
    }
}


