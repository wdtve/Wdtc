package org.wdt.wdtc.game.config;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.DownloadedGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.utils.JavaHomePath;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameConfig {
    private static final Logger logmaker = getWdtcLogger.getLogger(getWdtcLogger.class);
    private final Launcher launcher;

    public GameConfig(Launcher launcher) {
        this.launcher = launcher;
    }

    public static void writeConfigJsonToAllVersion() throws IOException {
        GetGamePath getGamePath = new GetGamePath();
        List<Launcher> list = DownloadedGameVersion.getGameVersionList(getGamePath);
        if (list != null) {
            for (Launcher child : list) {
                GameConfig config = child.getGameConfig();
                if (PlatformUtils.FileExistenceAndSize(config.getVersionConfigFile())) {
                    config.writeConfigJson();
                }
            }
        } else {
            logmaker.warn("* There are currently no game versions available");
        }
    }

    public File getVersionConfigFile() {
        return new File(launcher.getVersionPath() + "/config.json");
    }

    public String getJavaPath() {
        if (getVersionConfigObject().has("JavaPath")) {
            return getVersionConfigObject().getString("JavaPath");
        } else {
            return JavaHomePath.GetRunJavaHome();
        }
    }

    public int getRunningMemory() {
        if (getVersionConfigObject().has("RunningMemory")) {
            return getVersionConfigObject().getInt("RunningMemory");
        } else {
            return 1024;
        }
    }

    public int getWindowWidth() {
        if (getVersionConfigObject().has("WindowWidth")) {
            return getVersionConfigObject().getInt("WindowWidth");
        } else {
            return 855;
        }
    }

    public int getWindowHeight() {
        if (getVersionConfigObject().has("WindowHeight")) {
            return getVersionConfigObject().getInt("WindowHeight");
        } else {
            return 1000;
        }
    }

    public JSONObject getVersionConfigObject() {
        try {
            return JSONUtils.getJSONObject(getVersionConfigFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeConfigJson() throws IOException {
        DefaultGameConfig defaultconfig = new DefaultGameConfig();
        FileUtils.writeStringToFile(getVersionConfigFile(), JSONObject.toJSONString(defaultconfig), "UTF-8");
        logmaker.info("* " + launcher.getVersion() + " " + defaultconfig);

    }
}
