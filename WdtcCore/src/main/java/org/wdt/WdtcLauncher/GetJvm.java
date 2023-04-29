package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.FilePath;
import org.wdt.Version;
import org.wdt.WdtcDownload.GetGamePath;

import java.io.File;
import java.io.IOException;

public class GetJvm {

    private static final File l_j = FilePath.getLauncherJson();
    private static final File m_t = FilePath.getStarterBat();
    private static final File u_s = FilePath.getUsersSettingJson();
    private static final String java_home = GetJavaPath.GetRunJavaHome();
    private static String xmx = "1024";

    public static void read_jvm(String v) throws IOException {
        StringBuilder jvm_set = new StringBuilder();
        Version version = new Version(v);
        JSONObject u_j_e = JSON.parseObject(FileUtils.readFileToString(l_j, "UTF-8"));
        JSONObject u_s_j = JSONObject.parseObject(FileUtils.readFileToString(u_s, "UTF-8"));
        JSONArray users_jvm = u_s_j.getJSONArray("users_jvm");
        JSONArray user_game = u_s_j.getJSONArray("user_game");
        JSONArray jvm_j = u_j_e.getJSONArray("jvm");
        String game_p = GetGamePath.getGamePath();
        jvm_set.append("@echo off\ncd ").append(game_p).append("\n");
        jvm_set.append(java_home).append(" ");

        String log4j_path = jvm_j.getString(0) + version.getVersionLog4j2() + " ";
        jvm_set.append(log4j_path);

        String userxmx = user_game.getString(2);
//        if (userxmx != null) {
//            xmx = userxmx;
//        }

        String Xmx = jvm_j.getString(1) + xmx + "M ";
        jvm_set.append(Xmx);

        String client_jar_path = jvm_j.getString(2) + version.getVersionJar() + " ";
        jvm_set.append(client_jar_path);

        String run = jvm_j.getString(3) + " ";
        jvm_set.append(run);

        String userjvm = users_jvm.getString(0);
        if (userjvm != null) {
            jvm_set.append(userjvm);
        }

        String recovery = jvm_j.getString(4) + " ";
        jvm_set.append(recovery);


        File lib_pay = new File(version.getVersionNativesPath());
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
            String lib_path = jvm_j.getString(5) + version.getVersionNativesPath() + " ";
            jvm_set.append(lib_path);
        } else if (lib_pay.exists()) {
            String lib_path = jvm_j.getString(5) + version.getVersionNativesPath() + " ";
            jvm_set.append(lib_path);
        }

        String starter = jvm_j.getString(6) + " -cp ";
        jvm_set.append(starter);

        FileUtils.writeStringToFile(m_t, jvm_set.toString(), "UTF-8");
    }
}
