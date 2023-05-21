package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

//1.19-
public class DownloadGameLibFile extends DownloadTask {
    private static final Logger logmaker = Logger.getLogger(DownloadGameLibFile.class);
    private static Launcher version;

    public DownloadGameLibFile(Launcher launcher) {
        super(launcher);
        DownloadGameLibFile.version = launcher;
    }

    public void DownloadLibFile() throws IOException, RuntimeException {
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
                InputStream log4j2 = getClass().getResourceAsStream("/log4j2.xml");
                File v_log = new File(version.getVersionLog4j2());
                if (StringUtil.FileExistenceAndSize(v_log)) {
                    OutputStream vlog = new FileOutputStream(v_log);
                    IOUtils.copy(Objects.requireNonNull(log4j2), vlog);
                }
            } catch (RuntimeException | IOException e) {
                logmaker.error("* logej.xml不存在或路径错误!", e);
            }
            JSONObject client = v_e_j.getJSONObject("downloads").getJSONObject("client");
            try {
                URL jar_url = new URL(client.getString("url"));
                File VersionJar = new File(version.getVersionJar());
                if (StringUtil.FileExistenceAndSize(VersionJar)) {
                    StartDownloadTask(jar_url, VersionJar);
                }
            } catch (MalformedURLException | RuntimeException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }
}

