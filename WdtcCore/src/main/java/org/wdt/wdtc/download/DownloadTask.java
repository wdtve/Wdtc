package org.wdt.wdtc.download;


import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.launch.GameLibraryData;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends GameLibraryData {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadTask.class);
    private final Launcher launcher;
    private final DownloadSource source;

    public DownloadTask(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    @SneakyThrows(MalformedURLException.class)
    public static void StartDownloadTask(String url, String path) {
        StartDownloadTask(new URL(url), new File(path));
    }

    @SneakyThrows(MalformedURLException.class)
    public static void StartDownloadTask(String url, File file) {
        StartDownloadTask(new URL(url), file);
    }

    public static void StartDownloadTask(URL url, File file) {
        long Now = System.currentTimeMillis();
        DownloadUtils downloadUtils = new DownloadUtils(file, url);
        try {
            logmaker.info("Task Start: " + url);
            downloadUtils.ManyTimesToTryDownload(5);
            logmaker.info("Task Finish: " + file + ", Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
        } catch (Exception e) {
            logmaker.warn("Task: " + url, e);
            try {
                logmaker.info("Task: " + url + " Start retry");
                downloadUtils.ManyTimesToTryDownload(5);
                logmaker.info("Task: " + file + " Successfully retried, Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
            } catch (Exception exception) {
                if (file.delete()) {
                    logmaker.error("Task: " + url + " Error", exception);
                }
            }
        }
    }

    @SneakyThrows(IOException.class)
    public void StartDownloadHashTask(String hash, int Filesize, SpeedOfProgress downLatch, ThreadGroup group) {
        String HashHead = hash.substring(0, 2);
        File HashFile = new File(launcher.getGameObjects(), HashHead + "/" + hash);
        URL HashUrl = new URL(source.getAssetsUrl() + HashHead + "/" + hash);
        if (PlatformUtils.FileExistenceAndSize(HashFile, Filesize)) {
            Thread thread = new Thread(group, () -> {
                StartDownloadTask(HashUrl, HashFile);
                downLatch.countDown();
            });
            ThreadUtils.StartThread(thread).setName(hash);
        } else {
            downLatch.countDown();
        }
    }

    @SneakyThrows(IOException.class)
    public void StartDownloadLibraryTask(LibraryObject libraryObject, SpeedOfProgress speed) {
        File LibraryFile = GetLibraryFile(libraryObject);
        if (PlatformUtils.FileExistenceAndSize(LibraryFile, libraryObject.getDownloads().getArtifact().getSize())) {
            ThreadUtils.StartThread(() -> {
                StartDownloadTask(GetLibraryUrl(libraryObject), LibraryFile);
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
        if (PlatformUtils.FileExistenceAndSize(NativesLibrary, NativesWindows.getSize())) {
            ThreadUtils.StartThread(() -> {
                StartDownloadTask(GetNativesLibraryUrl(libraryObject), NativesLibrary);
                speed.countDown();
            });
        } else {
            speed.countDown();
        }
    }

}
