package org.WdtcDownload.DownloadLib;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.concurrent.Task;
import org.WdtcLauncher.FilePath;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

//1.19-
public class GetLibUrl extends GetLibPathUrl {
    private static final Logger logmaker = Logger.getLogger(GetLibUrl.class);
    private static Version version;

    public GetLibUrl(String version, boolean BMCLAPI) {
        super(BMCLAPI);
        GetLibUrl.version = new Version(version);
    }

    public void readdown() throws IOException, RuntimeException {
        File lib_pay = new File(version.getVersionNativesPath());
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
        }
        JSONObject v_e_j = JSONObject.parseObject(FileUtils.readFileToString(new File(version.getVersionJson()), "UTF-8"));
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (natives_j == null) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (rules == null) {
                    Task<Void> voidTask = new Task<>() {
                        @Override
                        protected Void call() {
                            try {
                                if (!readlib_path(lib_j).exists()) {
                                    logmaker.info("* " + readlib_url(lib_j) + " 开始下载");
                                    new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                    logmaker.info("* " + readlib_path(lib_j).getName() + "下载完成");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return null;
                        }

                    };
                    new Thread(voidTask).start();
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        Task<Void> voidTask = new Task<>() {
                            @Override
                            protected Void call() {

                                try {
                                    if (!readlib_path(lib_j).exists()) {
                                        logmaker.info("* " + readlib_url(lib_j) + " 开始下载");
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                        logmaker.info("* " + readlib_path(lib_j).getName() + "下载完成");
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }

                        };
                        new Thread(voidTask).start();
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        Task<Void> voidTask = new Task<>() {
                            @Override
                            protected Void call() {
                                try {
                                    if (!readlib_path(lib_j).exists()) {
                                        logmaker.info("* " + readlib_url(lib_j) + " 开始下载");
                                        new WGet(readlib_url(lib_j), readlib_path(lib_j)).download();
                                        logmaker.info("* " + readlib_path(lib_j).getName() + "下载完成");
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }
                        };
                        new Thread(voidTask).start();
                    }

                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (natives_name != null) {
                    Task<Void> voidTask = new Task<>() {
                        @Override
                        protected Void call() {
                            try {
                                if (!readnatives_lib(lib_j).exists()) {
                                    logmaker.info("* " + readnatives_url(lib_j) + " 开始下载");
                                    new WGet(readnatives_url(lib_j), readnatives_lib(lib_j)).download();
                                    logmaker.info("* " + readnatives_lib(lib_j).getName() + " 下载完成");
                                }
                            } catch (IOException | RuntimeException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    new Thread(voidTask).start();
                }
            }
        }
        Thread thread = new Thread(() -> {
            try {
                File log4j2 = new File(FilePath.getLog4j2());
                File v_log = new File(version.getVersionLog4j2());
                if (!v_log.exists()) {
                    FileUtils.copyFile(log4j2, v_log);
                }
            } catch (RuntimeException | IOException e) {
                logmaker.error("* logej.xml不存在或路径错误!");
            }
            JSONObject downloads_j = v_e_j.getJSONObject("downloads");
            JSONObject client = downloads_j.getJSONObject("client");
            try {
                URL jar_url = new URL(client.getString("url"));
                File VersionJar = new File(version.getVersionJar());
                if (!VersionJar.exists()) {
                    logmaker.info("* " + jar_url + " 开始下载");
                    new WGet(jar_url, VersionJar).download();
                    logmaker.info("* " + VersionJar.getName() + " 下载完成");
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }
}

