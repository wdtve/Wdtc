package org.wdt.wdtc.game.config;

import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.DownloadedGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GetGamePath;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameConfig {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcLogger.class);
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

    public DefaultGameConfig getGameConfig() {
        return JSONUtils.JsonFileToClass(getVersionConfigFile(), DefaultGameConfig.class);
    }

    public void writeConfigJson() throws IOException {
        DefaultGameConfig defaultconfig = new DefaultGameConfig();
        JSONUtils.ObjectToJsonFile(getVersionConfigFile(), defaultconfig);
        logmaker.info("* " + launcher.getVersion() + " " + defaultconfig);
    }

    public String getDefaultGameConfig() {
        return JSONObject.toJSONString(new DefaultGameConfig());
    }
}
