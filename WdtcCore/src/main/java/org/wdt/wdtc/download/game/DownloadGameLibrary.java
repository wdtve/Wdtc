package org.wdt.wdtc.download.game;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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
            InputStream log4j2 = getClass().getResourceAsStream("/log4j2.xml");
            File VersionLog = new File(launcher.getVersionLog4j2());
            if (PlatformUtils.FileExistenceAndSize(VersionLog)) {
                OutputStream vlog = new FileOutputStream(VersionLog);
                IOUtils.copy(Objects.requireNonNull(log4j2), vlog);
            }
        } catch (IOException e) {
            logmaker.error("* logej.xml不存在或路径错误!", e);
        }
    }
}


