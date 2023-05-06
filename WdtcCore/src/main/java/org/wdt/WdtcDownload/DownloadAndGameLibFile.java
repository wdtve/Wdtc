package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

//1.19-
public class DownloadAndGameLibFile extends DownloadTask {
    private static final Logger logmaker = Logger.getLogger(DownloadAndGameLibFile.class);
    private static Launcher version;

    public DownloadAndGameLibFile(Launcher launcher) {
        super(launcher);
        DownloadAndGameLibFile.version = launcher;
    }

    public void readdown() throws IOException, RuntimeException {
        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        JSONObject v_e_j = StringUtil.FileToJSONObject(version.getVersionJson());
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (Objects.isNull(natives_j)) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (Objects.isNull(rules)) {
                    new Thread(StartDownloadLibTask(lib_j)).start();
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        new Thread(StartDownloadLibTask(lib_j)).start();
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        new Thread(StartDownloadLibTask(lib_j)).start();
                    }

                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (Objects.nonNull(natives_name)) {
                    new Thread(StartDownloadNativesLibTask(lib_j)).start();
                }
            }
        }
        Thread thread = new Thread(() -> {
            try {
                File log4j2 = FilePath.getLog4j2();
                File v_log = new File(version.getVersionLog4j2());
                if (!v_log.exists()) {
                    FileUtils.copyFile(log4j2, v_log);
                }
            } catch (RuntimeException | IOException e) {
                logmaker.error("* logej.xml不存在或路径错误!");
            }
            JSONObject client = v_e_j.getJSONObject("downloads").getJSONObject("client");
            try {
                URL jar_url = new URL(client.getString("url"));
                File VersionJar = new File(version.getVersionJar());
                if (!VersionJar.exists()) {
                    StartDownloadTask(jar_url, VersionJar);
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }
}

