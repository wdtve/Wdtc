package org.wdt.wdtc.core.utils;

import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.manger.FileManger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadUtils {
    public static final File StopProcess = new File(FileManger.getWdtcCache(), "process");
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadUtils.class);
    private final File downloadFile;
    private final URL srcousFileURL;

    public DownloadUtils(File downloadFile, URL srcousFileURL) {
        this.downloadFile = downloadFile;
        this.srcousFileURL = srcousFileURL;
    }

    public static boolean isDownloadProcess() throws IOException {
        return FileUtils.isFileExists(StopProcess);
    }

    public static void StartDownloadTask(String url, String path) {
        StartDownloadTask(URLUtils.toURL(url), FileUtils.toFile(path));
    }

    public static void StartDownloadTask(String url, File file) {
        StartDownloadTask(URLUtils.toURL(url), file);
    }

    public static void StartDownloadTask(URL url, File file) {
        long Now = System.currentTimeMillis();
        DownloadUtils downloadUtils = new DownloadUtils(file, url);
        try {
            logmaker.info("Task Start: " + url);
            downloadUtils.manyTimesToTryDownload(5);
            logmaker.info("Task Finish: " + file + ", Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
        } catch (Exception e) {
            logmaker.warn("Task: " + url, e);
            try {
                logmaker.info("Task: " + url + " Start retry");
                downloadUtils.manyTimesToTryDownload(5);
                logmaker.info("Task: " + file + " Successfully retried, Take A Period Of " + (System.currentTimeMillis() - Now) + "ms");
            } catch (Exception exception) {
                if (file.delete()) {
                    logmaker.error("Task: " + url + " Error", exception);
                }
            }
        }
    }

    public void manyTimesToTryDownload(int times) {
        IOException exception = null;
        for (int i = 0; i < times; i++) {
            try {
                startDownloadFile();
                return;
            } catch (IOException e) {
                exception = e;
            }
        }
        if (exception != null) {
            logmaker.warn("Thread " + Thread.currentThread().getName() + " Error,", exception);
        }
    }

    public void startDownloadFile() throws IOException {
        FileUtils.touch(downloadFile);
        OutputStream downloadFileOutput = FileUtils.newOutputStream(downloadFile);
        InputStream urlFileInput = URLUtils.newInputStream(srcousFileURL);
        double donwloaded;
        Thread.currentThread().setName(downloadFile.getName());
        byte[] data = new byte[1024];
        while ((donwloaded = urlFileInput.read(data, 0, 1024)) >= 0) {
            if (isDownloadProcess()) {
                logmaker.debug(Thread.currentThread().getName() + " Stop");
                downloadFileOutput.close();
                urlFileInput.close();
                return;
            }
            downloadFileOutput.write(data, 0, (int) donwloaded);
        }
        downloadFileOutput.close();
        urlFileInput.close();
    }
}
