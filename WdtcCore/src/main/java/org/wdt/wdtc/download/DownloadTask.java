package org.wdt.wdtc.download;


import org.apache.log4j.Logger;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.LibraryObject;
import org.wdt.wdtc.launch.GameLibraryPathAndUrl;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.ThreadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends GameLibraryPathAndUrl {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadTask.class);
    private final Launcher launcher;
    private final DownloadSource source;

    public DownloadTask(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public static void StartDownloadTask(String url, String path) {
        try {
            StartDownloadTask(new URL(url), new File(path));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void StartDownloadTask(String url, File file) {
        try {
            StartDownloadTask(new URL(url), file);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void StartDownloadTask(URL url, File file) {
        long Now = System.currentTimeMillis();
        try {
            logmaker.info("* Task Start: " + url);
            DownloadUtils.ManyTimesToTryDownload(file, url, 5);
            logmaker.info("* Task Finish: " + file + ", Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
        } catch (Exception e) {
            logmaker.warn("* Task: " + url, e);
            try {
                logmaker.info("* Task: " + url + " Start retry");
                DownloadUtils.ManyTimesToTryDownload(file, url, 5);
                logmaker.info("* Task: " + file + " Successfully retried, Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
            } catch (Exception exception) {
                if (file.delete()) {
                    logmaker.error("* Task: " + url + " Error", exception);
                }
            }
        }
    }


    public Thread StartDownloadHashTask(String hash, int Filesize, SpeedOfProgress downLatch) {
        try {
            String hash_t = hash.substring(0, 2);
            File hash_path = new File(launcher.getGameObjects(), hash_t + "/" + hash);
            URL hash_url = new URL(source.getAssetsUrl() + hash_t + "/" + hash);
            return ThreadUtils.StartThread(() -> {
                try {
                    if (PlatformUtils.FileExistenceAndSize(hash_path, Filesize)) {
                        StartDownloadTask(hash_url, hash_path);
                    }
                } catch (IOException e) {
                    logmaker.error("", e);
                }
                downLatch.countDown();
            });
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Thread StartDownloadLibraryTask(LibraryObject libraryObject, SpeedOfProgress speed) {
        return ThreadUtils.StartThread(() -> {
            try {
                File LibraryFile = GetLibraryFile(libraryObject);
                if (PlatformUtils.FileExistenceAndSize(LibraryFile, libraryObject.getDownloads().getArtifact().getSize())) {
                    StartDownloadTask(GetLibraryUrl(libraryObject), LibraryFile);
                }
                speed.countDown();
            } catch (IOException e) {
                logmaker.error("* Error:", e);
            }

        });
    }

    public Thread StartDownloadNativesLibTask(LibraryObject libraryObject, SpeedOfProgress speed) {
        return ThreadUtils.StartThread(() -> {
            try {
                LibraryObject.NativesOs NativesWindows = libraryObject.getDownloads().getClassifiers().getNativesindows();
                File NativesLibrary = GetNativesLibraryFile(NativesWindows);
                if (PlatformUtils.FileExistenceAndSize(NativesLibrary, NativesWindows.getSize())) {
                    StartDownloadTask(GetNativesLibraryUrl(libraryObject), NativesLibrary);
                }
                speed.countDown();
            } catch (IOException e) {
                logmaker.error("* Error:", e);
            }

        });
    }

}
