package org.wdt.wdtc.launch;


import org.wdt.platform.gson.Utils;
import org.wdt.wdtc.auth.Accounts;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;

public class GetGame {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher launcher) throws IOException {
        GameSet = new StringBuilder();
        Accounts accounts = launcher.GetAccounts();
        append("--username");
        append(accounts.getUserName());
        append("--version");
        append(launcher.getVersion());
        append("--gameDir");
        append(launcher.getGamePath());
        append("--assetsDir");
        append(launcher.getGameAssetsdir());
        append("--assetIndex");
        append(Utils.getJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex").getString("id"));
        append("--uuid");
        append(accounts.getUserUUID());
        append("--accessToken");
        append(accounts.getAccessToken());
        append("--clientId");
        append("${clientid}");
        GameSet.append(AdditionalCommand.AdditionalGame(launcher));
        append("--versionType");
        GameSet.append("Wdtc-dome");
        launcher.setGameattribute(GameSet);
    }

    private static void append(String str) {
        GameSet.append(str).append(" ");
    }
}
