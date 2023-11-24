package org.wdt.wdtc.core.game.config;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.game.DownloadedGameVersion;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.GameDirectoryManger;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.IOException;
import java.util.List;

public class GameConfig {
  private static final Logger logmaker = WdtcLogger.getLogger(WdtcLogger.class);
  private final Launcher launcher;

  public GameConfig(Launcher launcher) {
    this.launcher = launcher;
  }

  public static void writeConfigJsonToAllVersion() throws IOException {
    GameDirectoryManger gameDirectoryManger = new GameDirectoryManger();
    if (DownloadedGameVersion.isDownloadedGame(gameDirectoryManger)) {
      List<Launcher> list = DownloadedGameVersion.getGameVersionList(gameDirectoryManger);
      for (Launcher child : list) {
        GameConfig config = child.getGameConfig();
        if (FileUtils.isFileNotExists(child.getVersionConfigFile())) {
          writeConfigJson(child);
        } else {
          DefaultGameConfig GameConfig = config.getDefaultGameConfig();
          GameConfig.setInfo(config.getVersionInfo());
          logmaker.info(GameConfig);
          config.putConfigToFile(GameConfig);
        }
      }
    } else {
      logmaker.warn("There are currently no game versions available");
    }
  }


  public static void writeConfigJson(Launcher launcher) {
    try {
      DefaultGameConfig config = new DefaultGameConfig(launcher);
      JsonUtils.writeObjectToFile(launcher.getVersionConfigFile(), config, Json.getBuilder().setPrettyPrinting());
      logmaker.info(launcher.getVersionNumber() + " " + config);
    } catch (IOException e) {
      logmaker.error(WdtcLogger.getExceptionMessage(e));
    }
  }

  public static void ckeckVersionInfo(Launcher launcher) throws IOException {
    DefaultGameConfig config = launcher.getGameConfig().getDefaultGameConfig();
    config.setInfo(launcher.getVersionInfo());
    JsonUtils.writeObjectToFile(launcher.getVersionConfigFile(), config, Json.getBuilder().setPrettyPrinting());
    logmaker.info(launcher.getVersionNumber() + " " + config);
  }

  public DefaultGameConfig.Config getConfig() {
    return getDefaultGameConfig().getConfig();
  }

  @SneakyThrows(IOException.class)
  public DefaultGameConfig getDefaultGameConfig() {
    return JsonUtils.readFileToClass(launcher.getVersionConfigFile(), DefaultGameConfig.class);
  }

  public VersionInfo getVersionInfo() {
    return getDefaultGameConfig().getInfo();

  }

  public void putConfigToFile(DefaultGameConfig config) throws IOException {
    JsonUtils.writeObjectToFile(launcher.getVersionConfigFile(), config, Json.getBuilder().setPrettyPrinting());
  }
}
