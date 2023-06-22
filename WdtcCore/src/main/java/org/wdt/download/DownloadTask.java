package org.wdt.download;

import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.Launcher;
import org.wdt.launch.GetLibraryPathAndUrl;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DownloadTask extends GetLibraryPathAndUrl {
    private static final Logger logmaker = Logger.getLogger(DownloadTask.class);
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
            logmaker.info("* " + url + " 开始下载");
            if (PlatformUtils.FileExistenceAndSize(file)) {
                FileUtils.copyURLToFile(url, file);
            }
            logmaker.info("* " + file + " 下载完成");
        } catch (IOException | InterruptedException e) {
            logmaker.error("* 下载任务" + url + "遇到错误,正在重试");
            try {
                TimeUnit.SECONDS.sleep(5);
                logmaker.info("* " + url + " 开始重试");
                FileUtils.copyURLToFile(url, file);
                logmaker.info("* " + file + " 重试成功");
            } catch (IOException | InterruptedException exception) {
                if (file.delete()) {
                    logmaker.error("* 下载任务" + url + " 重试失败,任务取消", exception);
                }
            }
        }
    }

    public static void StartWGetDownloadTask(URL url, File file) {
        WGet wGet = new WGet(url, file);
        try {
            logmaker.info("* " + url + " 开始下载");
            if (PlatformUtils.FileExistenceAndSize(file)) {
                wGet.download();
            }
            logmaker.info("* " + file + " 下载完成");
        } catch (RuntimeException | IOException exception) {
            logmaker.error("* 下载任务" + url + "遇到错误,正在重试");
            try {
                TimeUnit.SECONDS.sleep(5);
                logmaker.info("* " + url + " 开始重试");
                wGet.download();
                logmaker.info("* " + file + " 重试成功");
            } catch (InterruptedException | RuntimeException e) {
                if (file.delete()) {
                    logmaker.error("* 下载任务" + url + " 重试失败,任务取消", e);
                }
            }
        }
    }

    public static void StartWGetDownloadTask(String url, File file) {
        try {
            StartDownloadTask(new URL(url), file);
        } catch (MalformedURLException e) {
            logmaker.error("* 错误:", e);
        }
    }

    public static void StartWGetDownloadTask(String url, String file) {
        try {
            StartDownloadTask(new URL(url), new File(file));
        } catch (MalformedURLException e) {
            logmaker.error("* 错误:", e);
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
                logmaker.error("* 错误:", e);
            }

        });
    }

    public Thread StartDownloadNativesLibTask(JSONObject lib_j) {
        return new Thread(() -> {
            try {
                try {
                    StartDownloadTask(GetNativesLibUrl(lib_j), GetNativesLibPath(lib_j));
                } catch (MalformedURLException e) {
                    logmaker.error("* 错误:", e);
                }
            } catch (IOException e) {
                logmaker.error("* 错误:", e);
            }

        });
    }

}
