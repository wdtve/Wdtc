package org.wdt.wdtc.launch;


import com.google.gson.JsonElement;
import org.wdt.utils.FileUtils;
import org.wdt.wdtc.auth.User;
import org.wdt.wdtc.game.GameVersionJsonObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;
import org.wdt.wdtc.manger.VMManger;

import java.io.IOException;
import java.util.Map;

public class GameCommand {
    private final StringBuilder GameSet;
    private final Launcher launcher;

    public GameCommand(Launcher launcher) {
        this.launcher = launcher;
        this.GameSet = new StringBuilder();
    }

    public StringBuilder Getgame() throws IOException {
        DefaultGameConfig.Config gameConfig = launcher.getGameConfig().getConfig();
        GameVersionJsonObject VersionJsonObject = launcher.getGameVersionJsonObject();
        NonBreakingSpace(VersionJsonObject.getMainClass());
        for (JsonElement Element : VersionJsonObject.getArguments().getGameList()) {
            if (!Element.isJsonObject()) {
                NonBreakingSpace(ReplaceData(Element.getAsString()));
            }
        }
        NonBreakingSpace("--height");
        NonBreakingSpace(gameConfig.getWindowWidth());
        NonBreakingSpace("--width");
        GameSet.append(gameConfig.getWindowHeight());
        return GameSet;
    }

    private Map<String, String> getDataMap() throws IOException {
        User user = User.getUsers();
        return Map.of("${auth_player_name}", user.getUserName(), "${version_name}", launcher.getVersionNumber(),
                "${game_directory}", FileUtils.getCanonicalPath(launcher.getVersionPath()), "${assets_root}", FileUtils.getCanonicalPath(launcher.getGameAssetsdir()),
                "${assets_index_name}", launcher.getGameVersionJsonObject().getAssets(), "${auth_uuid}", user.getUuid(),
                "${auth_access_token}", user.getAccessToken(), "${user_type}", user.getType().toString(),
                "${version_type}", "Wdtc-" + VMManger.getLauncherVersion());
    }

    private void NonBreakingSpace(Object o) {
        GameSet.append(o).append(" ");
    }

    private String ReplaceData(String str) throws IOException {
        Map<String, String> ReplaceMap = getDataMap();
        for (String s : ReplaceMap.keySet()) {
            str = str.replace(s, ReplaceMap.get(s));
        }
        return str;
    }
}
