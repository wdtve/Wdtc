package org.wdt.wdtc.game.config;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSON;
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
        if (DownloadedGameVersion.isDownloadedGame(getGamePath)) {
            List<Launcher> list = DownloadedGameVersion.getGameVersionList(getGamePath);
            for (Launcher child : list) {
                GameConfig config = child.getGameConfig();
                if (PlatformUtils.FileExistenceAndSize(config.getVersionConfigFile())) {
                    writeConfigJson(child);
                } else {
                    DefaultGameConfig GameConfig = config.getDefaultGameConfig();
                    GameConfig.setInfo(config.getVersionInfo());
                    logmaker.info(GameConfig);
                    config.PutConfigToFile(GameConfig);
                }
            }
        } else {
            logmaker.warn("* There are currently no game versions available");
        }
    }

    public File getVersionConfigFile() {
        return new File(launcher.getVersionPath() + "/config.json");
    }

    public static void writeConfigJson(Launcher launcher) {
        try {
            GameConfig gameConfig = launcher.getGameConfig();
            DefaultGameConfig config = new DefaultGameConfig();
            config.setConfig(new DefaultGameConfig.Config());
            config.setInfo(launcher.getVersionInfo());
            FileUtils.writeStringToFile(gameConfig.getVersionConfigFile(), JSON.GSONBUILDER.serializeNulls().setPrettyPrinting().create().toJson(config), "UTF-8");
            logmaker.info("* " + launcher.getVersion() + " " + config);
        } catch (IOException e) {
            logmaker.error("", e);
        }
    }

    public DefaultGameConfig.Config getConfig() {
        return getDefaultGameConfig().getConfig();
    }

    public DefaultGameConfig getDefaultGameConfig() {
        try {
            return JSONUtils.JsonFileToClass(getVersionConfigFile(), DefaultGameConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VersionInfo getVersionInfo() {
        try {
            return JSONUtils.JsonFileToClass(getVersionConfigFile(), DefaultGameConfig.class).getInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void PutConfigToFile(DefaultGameConfig config) {
        JSONUtils.ObjectToJsonFile(getVersionConfigFile(), config);
    }

}
