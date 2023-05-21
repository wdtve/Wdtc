package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.IOException;

public class GetGame {

    public static void Getgame(Launcher version) throws IOException {
        Accounts accounts = version.GetAccounts();
        StringBuilder game_set = new StringBuilder();
        JSONObject assetIndex_j = StringUtil.FileToJSONObject(version.getVersionJson()).getJSONObject("assetIndex");
        game_set.append("--username").append(" ").append(accounts.GetUserName()).append(" ");
        game_set.append("--version").append(" ").append(version.getVersion()).append(" ");
        game_set.append("--gameDir").append(" ").append(version.getGamePath()).append(" ");
        game_set.append("--assetsDir").append(" ").append(version.getGameAssetsdir()).append(" ");
        game_set.append("--assetIndex").append(" ").append(assetIndex_j.getString("id")).append(" ");
        game_set.append("--uuid").append(" ").append(accounts.GetUserUUID()).append(" ");
        game_set.append("--accessToken").append(" ").append(accounts.GetAccessToken()).append(" ");
        game_set.append("--clientId").append(" ").append("${clientid}").append(" ");
        game_set.append("--versionType").append(" ").append("Wdtc-dome");
        version.setGameattribute(game_set);
    }
}
