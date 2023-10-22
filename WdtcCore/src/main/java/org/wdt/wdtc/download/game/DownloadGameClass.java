package org.wdt.wdtc.download.game;


import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.download.SpeedOfProgress;
import org.wdt.wdtc.game.GetGameNeedLibraryFile;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.launch.GameLibraryData;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class DownloadGameClass extends GameLibraryData {
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
            logmaker.error("Download Library File Error,", e);
        }
        try {
            File VersionLog = launcher.getVersionLog4j2();
            FileUtils.writeStringToFile(VersionLog,
                    IOUtils.toString(Objects.requireNonNull(DownloadGameClass.class.getResourceAsStream("/log4j2.xml"))));
        } catch (IOException e) {
            logmaker.error("logej.xml不存在或路径错误!", e);
        }
    }

    @SneakyThrows(IOException.class)
    public void StartDownloadLibraryTask(LibraryObject libraryObject, SpeedOfProgress speed) {
        File LibraryFile = GetLibraryFile(libraryObject);
        if (FileUtils.isFileNotExistsAndIsNotSameSize(LibraryFile, libraryObject.getDownloads().getArtifact().getSize())) {
            ThreadUtils.startThread(() -> {
                DownloadUtils.StartDownloadTask(GetLibraryUrl(libraryObject), LibraryFile);
                speed.countDown();
            });
        } else {
            speed.countDown();
        }


    }

    @SneakyThrows(IOException.class)
    public void StartDownloadNativesLibTask(LibraryObject libraryObject, SpeedOfProgress speed) {
        LibraryObject.NativesOs NativesWindows = libraryObject.getDownloads().getClassifiers().getNativesindows();
        File NativesLibrary = GetNativesLibraryFile(NativesWindows);
        if (FileUtils.isFileNotExistsAndIsNotSameSize(NativesLibrary, NativesWindows.getSize())) {
            ThreadUtils.startThread(() -> {
                DownloadUtils.StartDownloadTask(GetNativesLibraryUrl(libraryObject), NativesLibrary);
                speed.countDown();
            });
        } else {
            speed.countDown();
        }
    }
}


