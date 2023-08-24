package org.wdt.wdtc.game.config;

import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSON;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.game.DownloadedGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.launch.GamePath;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.List;

public class GameConfig {
    private static final Logger logmaker = WdtcLogger.getLogger(WdtcLogger.class);
    private final Launcher launcher;

    public GameConfig(Launcher launcher) {
        this.launcher = launcher;
    }

    public static void writeConfigJsonToAllVersion() throws IOException {
        GamePath gamePath = new GamePath();
        if (DownloadedGameVersion.isDownloadedGame(gamePath)) {
            List<Launcher> list = DownloadedGameVersion.getGameVersionList(gamePath);
            for (Launcher child : list) {
                GameConfig config = child.getGameConfig();
                if (PlatformUtils.FileExistenceAndSize(child.getVersionConfigFile())) {
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



    public static void writeConfigJson(Launcher launcher) {
        try {
            DefaultGameConfig config = new DefaultGameConfig();
            config.setConfig(new DefaultGameConfig.Config());
            config.setInfo(launcher.getVersionInfo());
            FileUtils.writeStringToFile(launcher.getVersionConfigFile(), JSON.GSONBUILDER.serializeNulls().setPrettyPrinting().create().toJson(config));
            logmaker.info("* " + launcher.getVersionNumber() + " " + config);
        } catch (IOException e) {
            logmaker.error("", e);
        }
    }

    public DefaultGameConfig.Config getConfig() {
        return getDefaultGameConfig().getConfig();
    }

    public DefaultGameConfig getDefaultGameConfig() {
        try {
            return JSONUtils.JsonFileToClass(launcher.getVersionConfigFile(), DefaultGameConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VersionInfo getVersionInfo() {
        try {
            return JSONUtils.JsonFileToClass(launcher.getVersionConfigFile(), DefaultGameConfig.class).getInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void PutConfigToFile(DefaultGameConfig config) {
        JSONUtils.ObjectToJsonFile(launcher.getVersionConfigFile(), config);
    }

}
