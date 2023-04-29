package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONObject;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DownloadTask extends GetLibPathAndUrl {
    private static final Logger logmaker = Logger.getLogger(DownloadTask.class);
    private static boolean bmclapi = false;

    public DownloadTask(boolean bmclapi) {
        super(bmclapi);
        DownloadTask.bmclapi = bmclapi;
    }

    public static void StartDownloadTask(URL url, File file) {
        try {
            Thread.sleep(20);
            logmaker.info("* " + url + " 开始下载");
            FileUtils.copyURLToFile(url, file);
            logmaker.info("* " + file + " 下载完成");
        } catch (IOException | InterruptedException e) {
            logmaker.error("* 下载任务" + url + "遇到错误,正在重试");
            try {
                TimeUnit.SECONDS.sleep(5);
                logmaker.info("* " + url + " 开始重试");
                FileUtils.copyURLToFile(url, file);
                logmaker.info("* " + file + " 重试成功");
            } catch (IOException | InterruptedException exception) {
                logmaker.error("* 下载任务" + url + " 重试失败,任务取消");
            }
        }
    }

    public static Task<Void> StartDownloadLibTask(JSONObject lib_j) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                if (!readlib_path(lib_j).exists()) {
                    StartDownloadTask(readlib_url(lib_j), readlib_path(lib_j));
                }
                return null;
            }
        };
    }

    public static Task<Void> StartDownloadNativesLibTask(JSONObject lib_j) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                if (!readnatives_lib(lib_j).exists()) {
                    StartDownloadTask(readnatives_url(lib_j), readnatives_lib(lib_j));
                }
                return null;
            }
        };
    }


    public static Runnable StartDownloadHashTask(String hash, CountDownLatch downLatch) throws MalformedURLException {
        String hash_t = hash.substring(0, 2);
        File hash_path = new File(GetGamePath.getGameAssetsdir() + "objects\\" + hash_t + "\\" + hash);
        URL hash_url;
        if (bmclapi) {
            hash_url = new URL(FileUrl.getBmclapiAssets() + hash_t + "/" + hash);
        } else {
            hash_url = new URL(FileUrl.getMojangAssets() + hash_t + "/" + hash);
        }
        Runnable runnable = () -> {
            if (!hash_path.exists()) {
                StartDownloadTask(hash_url, hash_path);
                downLatch.countDown();
            } else {
                downLatch.countDown();
            }
        };
        downLatch.countDown();
        return runnable;
    }

}
