package org.WdtcLauncher.GameSet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GetGame {
    private static final File m_t = new File("WdtcCore/ResourceFile/Launcher/starter.bat");
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final File l_j = new File("WdtcCore/ResourceFile/Launcher/launcher.json");
    private static final File u_j = new File("WdtcCore/ResourceFile/Launcher/users/users.json");
    private static final String users_uuid = String.valueOf(UUID.randomUUID()).replaceAll("-", "");

    public static void Getgame(String v, File v_j) throws IOException {
        StringBuffer game_set = new StringBuffer();
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_j = JSON.parseObject(s_e);
        String l_e = FileUtils.readFileToString(l_j);
        JSONObject l_e_j = JSON.parseObject(l_e);
        JSONArray game_j = l_e_j.getJSONArray("game");
        String v_e = FileUtils.readFileToString(v_j);
        JSONObject v_e_j = JSONObject.parseObject(v_e);
        JSONObject assetIndex_j = v_e_j.getJSONObject("assetIndex");
        String u_e = FileUtils.readFileToString(u_j);
        JSONObject u_e_j = JSONObject.parseObject(u_e);
        String user_name = u_e_j.getString("user_name");

        String usersname = " " + game_j.getString(0) + " " + user_name;
        game_set.append(usersname);

        String version_set = " " + game_j.getString(1) + " " + v;
        game_set.append(version_set);

        String gamedir = " " + game_j.getString(2) + " " + s_e_j.getString("game_path");
        game_set.append(gamedir);

        String assersdir = " " + game_j.getString(3) + " " + s_e_j.getString("Game_assetsDir");
        game_set.append(assersdir);

        String assetIndex = " " + game_j.getString(4) + " " + assetIndex_j.getString("id");
        game_set.append(assetIndex);

        String Uuid = " " + game_j.getString(5) + " " + users_uuid;
        game_set.append(Uuid);

        String accesstoken = " " + game_j.getString(6) + " " + "${auth_access_token}";
        game_set.append(accesstoken);

        String clientid = " " + game_j.getString(7) + " " + "${clientid}";
        game_set.append(clientid);

        String xuid = " " + game_j.getString(8) + " " + "${auth_xuid}";
        game_set.append(xuid);

        String type = " " + game_j.getString(9) + " " + "legacy";
        game_set.append(type);

        String v_type = " " + game_j.getString(10) + " " + "Wdtc-dome";
        game_set.append(v_type);
//        game_set.append("\npause");
        FileUtils.writeStringToFile(m_t, game_set.toString(), true);

    }
}
