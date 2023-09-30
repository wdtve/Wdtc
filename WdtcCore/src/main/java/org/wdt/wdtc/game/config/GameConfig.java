package org.wdt.wdtc.game.config;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.JSON;
import org.wdt.utils.gson.JSONUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.game.DownloadedGameVersion;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.manger.GameFolderManger;
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
        GameFolderManger gameFolderManger = new GameFolderManger();
        if (DownloadedGameVersion.isDownloadedGame(gameFolderManger)) {
            List<Launcher> list = DownloadedGameVersion.getGameVersionList(gameFolderManger);
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
            logmaker.warn("There are currently no game versions available");
        }
    }


    public static void writeConfigJson(Launcher launcher) {
        try {
            DefaultGameConfig config = new DefaultGameConfig(launcher);
            FileUtils.writeStringToFile(launcher.getVersionConfigFile(), JSON.GSONBUILDER.serializeNulls().setPrettyPrinting().create().toJson(config));
            logmaker.info(launcher.getVersionNumber() + " " + config);
        } catch (IOException e) {
            logmaker.error(WdtcLogger.getErrorMessage(e));
        }
    }

    public DefaultGameConfig.Config getConfig() {
        return getDefaultGameConfig().getConfig();
    }

    public static void CkeckVersionInfo(Launcher launcher) throws IOException {
        DefaultGameConfig config = launcher.getGameConfig().getDefaultGameConfig();
        config.setInfo(launcher.getVersionInfo());
        FileUtils.writeStringToFile(launcher.getVersionConfigFile(), JSON.GSONBUILDER.serializeNulls().setPrettyPrinting().create().toJson(config));
        logmaker.info(launcher.getVersionNumber() + " " + config);
    }

    @SneakyThrows(IOException.class)
    public DefaultGameConfig getDefaultGameConfig() {
        return JSONUtils.JsonFileToClass(launcher.getVersionConfigFile(), DefaultGameConfig.class);
    }

    @SneakyThrows(IOException.class)
    public VersionInfo getVersionInfo() {
        return JSONUtils.JsonFileToClass(launcher.getVersionConfigFile(), DefaultGameConfig.class).getInfo();

    }

    public void PutConfigToFile(DefaultGameConfig config) {
        JSONUtils.ObjectToJsonFile(launcher.getVersionConfigFile(), config);
    }
}
