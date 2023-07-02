package org.wdt.launch;


import org.wdt.auth.Accounts;
import org.wdt.game.Launcher;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;

import java.io.IOException;

public class GetGame {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher launcher) throws IOException {
        GameSet = new StringBuilder();
        Accounts accounts = launcher.GetAccounts();
        JSONObject AssetIndexJson = Utils.getJSONObject(launcher.getVersionJson()).getJSONObject("assetIndex");
        append("--username");
        append(accounts.GetUserName());
        append("--version");
        append(launcher.getVersion());
        append("--gameDir");
        append(launcher.getGamePath());
        append("--assetsDir");
        append(launcher.getGameAssetsdir());
        append("--assetIndex");
        append(AssetIndexJson.getString("id"));
        append("--uuid");
        append(accounts.GetUserUUID());
        append("--accessToken");
        append(accounts.GetAccessToken());
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
