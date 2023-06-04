package org.wdt.launch;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.Launcher;
import org.wdt.auth.Accounts;
import org.wdt.download.forge.ForgeLaunchTask;
import org.wdt.platform.PlatformUtils;

import java.io.IOException;

public class GetGame {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher version) throws IOException {
        GameSet = new StringBuilder();
        Accounts accounts = version.GetAccounts();
        JSONObject AssetIndexJson = PlatformUtils.FileToJSONObject(version.getVersionJson()).getJSONObject("assetIndex");
        ForgeLaunchTask forgeLaunchTask = version.getForgeLaunchTask();
        append("--username");
        append(accounts.GetUserName());
        append("--version");
        append(version.getVersion());
        append("--gameDir");
        append(version.getGamePath());
        append("--assetsDir");
        append(version.getGameAssetsdir());
        append("--assetIndex");
        append(AssetIndexJson.getString("id"));
        append("--uuid");
        append(accounts.GetUserUUID());
        append("--accessToken");
        append(accounts.GetAccessToken());
        append("--clientId");
        append("${clientid}");
        if (version.getForgeDownloadTaskIsNull()) {
            for (String s : forgeLaunchTask.getForgeLaunchGame()) {
                append(s);
            }
        }
        append("--versionType");
        GameSet.append("Wdtc-dome");
        version.setGameattribute(GameSet);
    }

    private static void append(String str) {
        GameSet.append(str).append(" ");
    }
}
