package org.wdt.download;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.Launcher;
import org.wdt.platform.PlatformUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class DownloadGameLibrary extends DownloadTask {
    private final Logger logmaker = Logger.getLogger(DownloadGameLibrary.class);
    private final Launcher version;

    public DownloadGameLibrary(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadLibraryFile() throws IOException, RuntimeException {
        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        JSONArray LibraryList = PlatformUtils.FileToJSONObject(version.getVersionJson()).getJSONArray("libraries");
        for (int i = 0; i < LibraryList.size(); i++) {
            JSONObject LibraryJSONObject = LibraryList.getJSONObject(i);
            JSONObject NativesJson = LibraryJSONObject.getJSONObject("natives");
            if (Objects.isNull(NativesJson)) {
                JSONArray rules = LibraryJSONObject.getJSONArray("rules");
                if (Objects.isNull(rules)) {
                    StartDownloadLibTask(LibraryJSONObject).start();
                } else {
                    JSONObject ActionJson = rules.getJSONObject(rules.size() - 1);
                    String action = ActionJson.getString("action");
                    String OSName = ActionJson.getJSONObject("os").getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(OSName, "osx")) {
                        StartDownloadLibTask(LibraryJSONObject).start();
                    } else if (Objects.equals(action, "allow") && Objects.equals(OSName, "windows")) {
                        StartDownloadLibTask(LibraryJSONObject).start();
                    }
                }
            } else {
                if (Objects.nonNull(NativesJson.getString("windows"))) {
                    StartDownloadNativesLibTask(LibraryJSONObject).start();
                }
            }
        }
        try {
            InputStream log4j2 = getClass().getResourceAsStream("/log4j2.xml");
            File VersionLog = new File(version.getVersionLog4j2());
            if (PlatformUtils.FileExistenceAndSize(VersionLog)) {
                OutputStream vlog = new FileOutputStream(VersionLog);
                IOUtils.copy(Objects.requireNonNull(log4j2), vlog);
            }
        } catch (IOException e) {
            logmaker.error("* logej.xml不存在或路径错误!", e);
        }

    }
}

