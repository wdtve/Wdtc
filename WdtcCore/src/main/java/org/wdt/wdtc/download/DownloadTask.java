package org.wdt.wdtc.download;


import com.github.axet.wget.WGet;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GameLibraryPathAndUrl;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.log4j.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DownloadTask extends GameLibraryPathAndUrl {
    private static final Logger logmaker = getWdtcLogger.getLogger(DownloadTask.class);
    private final Launcher launcher;

    public DownloadTask(Launcher launcher) {
        super(launcher);
        this.launcher = launcher;
    }

    public static void StartDownloadTask(String url, String path) throws MalformedURLException {
        StartDownloadTask(new URL(url), new File(path));
    }

    public static void StartDownloadTask(String url, File file) throws MalformedURLException {
        StartWGetDownloadTask(new URL(url), file);
    }

    public static void StartDownloadTask(URL url, File file) {
        try {
            Thread.sleep(20);
            logmaker.info("* Task Start: " + url);
            if (PlatformUtils.FileExistenceAndSize(file)) {
                FileUtils.copyURLToFile(url, file);
            }
            logmaker.info("* Task Finish: " + file);
        } catch (IOException | InterruptedException e) {
            logmaker.warn("* Task: " + url, e);
            try {
                TimeUnit.SECONDS.sleep(5);
                logmaker.info("* Task: " + url + " Start retry");
                FileUtils.copyURLToFile(url, file);
                logmaker.info("* Task: " + file + " Successfully retried");
            } catch (IOException | InterruptedException exception) {
                if (file.delete()) {
                    logmaker.error("* Task: " + url + " Error", exception);
                }
            }
        }
    }

    public static void StartWGetDownloadTask(URL url, File file) {
        WGet wGet = new WGet(url, file);
        try {
            logmaker.info("* Task Start: " + url);
            if (PlatformUtils.FileExistenceAndSize(file)) {
                wGet.download();
            }
            logmaker.info("* Task Finish: " + file);
        } catch (RuntimeException | IOException exception) {
            logmaker.warn("* Task: " + url, exception);
            try {
                TimeUnit.SECONDS.sleep(5);
                logmaker.info("* Task: " + url + " Start retry");
                wGet.download();
                logmaker.info("* Task: " + file + " Successfully retried");
            } catch (InterruptedException | RuntimeException e) {
                if (file.delete()) {
                    logmaker.error("* Task: " + url + " Error", e);
                }
            }
        }
    }

    public static void StartWGetDownloadTask(String url, File file) {
        try {
            StartDownloadTask(new URL(url), file);
        } catch (MalformedURLException e) {
            logmaker.error("* Error:", e);
        }
    }

    public static void StartWGetDownloadTask(String url, String file) {
        try {
            StartDownloadTask(new URL(url), new File(file));
        } catch (MalformedURLException e) {
            logmaker.error("* Error:", e);
        }
    }

    public Thread StartDownloadHashTask(String hash, SpeedOfProgress downLatch) throws IOException {
        String hash_t = hash.substring(0, 2);
        File hash_path = new File(launcher.getGameObjects() + hash_t + "\\" + hash);
        URL hash_url;
        if (launcher.bmclapi()) {
            hash_url = new URL(FileUrl.getBmclapiAssets() + hash_t + "/" + hash);
        } else {
            hash_url = new URL(FileUrl.getMojangAssets() + hash_t + "/" + hash);
        }
        return new Thread(() -> {
            StartDownloadTask(hash_url, hash_path);
            downLatch.countDown();
        });
    }

    public Thread StartDownloadLibTask(JSONObject lib_j) {
        return new Thread(() -> {
            try {
                StartDownloadTask(GetLibUrl(lib_j), GetLibPath(lib_j));
            } catch (MalformedURLException e) {
                logmaker.error("* Error:", e);
            }

        });
    }

    public Thread StartDownloadNativesLibTask(JSONObject lib_j) {
        return new Thread(() -> {
            try {
                try {
                    StartDownloadTask(GetNativesLibUrl(lib_j), GetNativesLibPath(lib_j));
                } catch (MalformedURLException e) {
                    logmaker.error("* Error:", e);
                }
            } catch (IOException e) {
                logmaker.error("* Error:", e);
            }

        });
    }

}
