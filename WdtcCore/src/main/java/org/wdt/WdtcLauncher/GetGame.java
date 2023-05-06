package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GetGame {
    private static final File u_j = FilePath.getUsersJson();
    private static final String users_uuid = String.valueOf(UUID.randomUUID()).replaceAll("-", "");

    public static void Getgame(Launcher version) throws IOException {
        StringBuilder game_set = new StringBuilder();
        JSONObject assetIndex_j = StringUtil.FileToJSONObject(version.getVersionJson()).getJSONObject("assetIndex");
        String user_name = StringUtil.FileToJSONObject(u_j).getString("user_name");
        game_set.append("--username").append(" ").append(user_name).append(" ");
        game_set.append("--version").append(" ").append(version.getVersion()).append(" ");
        game_set.append("--gameDir").append(" ").append(version.getGamePath()).append(" ");
        game_set.append("--assetsDir").append(" ").append(version.getGameAssetsdir()).append(" ");
        game_set.append("--assetIndex").append(" ").append(assetIndex_j.getString("id")).append(" ");
        game_set.append("--uuid").append(" ").append(users_uuid).append(" ");
        game_set.append("--accessToken").append(" ").append("${auth_access_token}").append(" ");
        game_set.append("--clientId").append(" ").append("${clientid}").append(" ");
        game_set.append("--xuid").append(" ").append("${auth_xuid}").append(" ");
        game_set.append("--versionType").append(" ").append("Wdtc-dome");
        version.setGameattribute(game_set);
    }
}
