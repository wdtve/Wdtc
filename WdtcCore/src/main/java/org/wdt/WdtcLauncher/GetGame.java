package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.IOException;

public class GetGame {
    private static StringBuilder GameSet;

    public static void Getgame(Launcher version) throws IOException {
        GameSet = new StringBuilder();
        Accounts accounts = version.GetAccounts();
        JSONObject assetIndex_j = StringUtil.FileToJSONObject(version.getVersionJson()).getJSONObject("assetIndex");
        append("--username");
        append(accounts.GetUserName());
        append("--version");
        append(version.getVersion());
        append("--gameDir");
        append(version.getGamePath());
        append("--assetsDir");
        append(version.getGameAssetsdir());
        append("--assetIndex");
        append(assetIndex_j.getString("id"));
        append("--uuid");
        append(accounts.GetUserUUID());
        append("--accessToken");
        append(accounts.GetAccessToken());
        append("--clientId");
        append("${clientid}");
        append("--versionType");
        GameSet.append("Wdtc-dome");
        version.setGameattribute(GameSet);
    }

    private static void append(String str) {
        GameSet.append(str).append(" ");
    }
}
