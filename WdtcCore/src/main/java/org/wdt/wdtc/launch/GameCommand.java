package org.wdt.wdtc.launch;


import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.auth.Users;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.DefaultGameConfig;

import java.io.IOException;

public class GameCommand {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher launcher) throws IOException {
        GameSet = new StringBuilder();
        DefaultGameConfig gameConfig = launcher.getGameConfig().getGameConfig();
        Users accounts = launcher.GetAccounts().getUsers();
        append("--username");
        append(accounts.getUserName());
        append("--version");
        append(launcher.getVersion());
        append("--gameDir");
        append(launcher.getVersionPath());
        append("--assetsDir");
        append(launcher.getGameAssetsdir());
        append("--assetIndex");
        append(JSONUtils.getJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex").getString("id"));
        append("--uuid");
        append(accounts.getUuid());
        append("--accessToken");
        append(accounts.getAccessToken());
        append("--clientId");
        append("${clientid}");
        GameSet.append(AdditionalCommand.AdditionalGame(launcher));
        append("--versionType");
        append("Wdtc-dome");
        append("--height");
        append(gameConfig.getWindowWidth());
        append("--width");
        GameSet.append(gameConfig.getWindowHeight());
        launcher.setGameattribute(GameSet);
    }

    private static void append(Object o) {
        GameSet.append(o).append(" ");
    }
}
