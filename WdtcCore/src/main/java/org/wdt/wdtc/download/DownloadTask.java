package org.wdt.wdtc.download;


import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GameLibraryData;
import org.wdt.wdtc.utils.DownloadUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends GameLibraryData {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadTask.class);

    public DownloadTask(Launcher launcher) {
        super(launcher);

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




}
