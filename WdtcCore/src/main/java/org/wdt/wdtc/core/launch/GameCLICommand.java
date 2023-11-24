package org.wdt.wdtc.core.launch;


import com.google.gson.JsonElement;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.auth.User;
import org.wdt.wdtc.core.game.GameVersionJsonObject;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.game.config.DefaultGameConfig;
import org.wdt.wdtc.core.manger.VMManger;

import java.io.IOException;
import java.util.Map;

public class GameCLICommand extends AbstractGameCommand {
  private final Launcher launcher;

  public GameCLICommand(Launcher launcher) {
    this.launcher = launcher;
  }

  public StringBuilder getCommand() throws IOException {
    DefaultGameConfig.Config gameConfig = launcher.getGameConfig().getConfig();
    GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
    nonBreakingSpace(VersionJsonObject.getMainClass());
    for (JsonElement Element : VersionJsonObject.getArguments().getGameList()) {
      if (!Element.isJsonObject()) {
        nonBreakingSpace(ReplaceData(Element.getAsString()));
      }
    }
    nonBreakingSpace("--height");
    nonBreakingSpace(gameConfig.getHight());
    nonBreakingSpace("--width");
    command.append(gameConfig.getWidth());
    return command;
  }

  private Map<String, String> getDataMap() throws IOException {
    User user = User.getUser();
    return Map.of("${auth_player_name}", user.getUserName(), "${version_name}", launcher.getVersionNumber(),
        "${game_directory}", FileUtils.getCanonicalPath(launcher.getVersionPath()), "${assets_root}", FileUtils.getCanonicalPath(launcher.getGameAssetsDirectory()),
        "${assets_index_name}", launcher.getGameVersionJsonObject().getAssets(), "${auth_uuid}", user.getUuid(),
        "${auth_access_token}", user.getAccessToken(), "${user_type}", user.getType().toString(),
        "${version_type}", "Wdtc-" + VMManger.getLauncherVersion());
  }


  private String ReplaceData(String str) throws IOException {
    Map<String, String> ReplaceMap = getDataMap();
    for (String s : ReplaceMap.keySet()) {
      str = str.replace(s, ReplaceMap.get(s));
    }
    return str;
  }
}