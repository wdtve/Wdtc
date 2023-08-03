package org.wdt.wdtc.download.game;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class DownloadGameLibrary extends DownloadTask {
    private final Logger logmaker = WdtcLogger.getLogger(DownloadGameLibrary.class);
    private final Launcher version;

    public DownloadGameLibrary(Launcher launcher) {
        super(launcher);
        this.version = launcher;
    }

    public void DownloadLibraryFile() {
        try {
            Files.createDirectories(Paths.get(version.getVersionNativesPath()));
            JSONArray LibraryList = JSONUtils.getJSONObject(version.getVersionJson()).getJSONArray("libraries");
            for (int i = 0; i < LibraryList.size(); i++) {
                JSONObject LibraryJSONObject = LibraryList.getJSONObject(i);
                if (LibraryJSONObject.has("natives")) {
                    JSONObject NativesJson = LibraryJSONObject.getJSONObject("natives");
                    if (NativesJson.has("windows")) {
                        StartDownloadNativesLibTask(LibraryJSONObject);
                    }
                } else {
                    if (LibraryJSONObject.has("rules")) {
                        JSONArray rules = LibraryJSONObject.getJSONArray("rules");
                        JSONObject ActionJson = rules.getJSONObject(rules.size() - 1);
                        String action = ActionJson.getString("action");
                        String OSName = ActionJson.getJSONObject("os").getString("name");
                        if (Objects.equals(action, "disallow") && Objects.equals(OSName, "osx")) {
                            StartDownloadLibTask(LibraryJSONObject);
                        } else if (Objects.equals(action, "allow") && Objects.equals(OSName, "windows")) {
                            StartDownloadLibTask(LibraryJSONObject);
                        }
                    } else {
                        StartDownloadLibTask(LibraryJSONObject);
                    }
                }
            }
        } catch (IOException e) {
            logmaker.error("* Download Library File Error,", e);
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


