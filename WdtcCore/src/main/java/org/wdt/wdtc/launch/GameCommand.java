package org.wdt.wdtc.launch;


import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.config.GameConfig;

import java.io.IOException;

public class GameCommand {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher launcher) throws IOException {
        GameSet = new StringBuilder();
        GameConfig gameConfig = launcher.getGameConfig();
        Accounts accounts = launcher.GetAccounts();
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
        append(accounts.getUserUUID());
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
