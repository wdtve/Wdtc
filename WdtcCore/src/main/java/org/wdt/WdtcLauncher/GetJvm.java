package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetJvm {

    private static final File l_j = FilePath.getLauncherJson();
    private static final String java_home = GetJavaPath.GetRunJavaHome();
    private static String xmx = "1024";

    public static void readJvm(Launcher version) throws IOException {
        StringBuilder jvm_set = new StringBuilder();
        JSONObject u_j_e = StringUtil.FileToJSONObject(l_j);
        JSONArray jvm_j = u_j_e.getJSONArray("jvm");
        String game_p = version.getGamePath();
        jvm_set.append("@echo off\ncd ").append(game_p).append("\n");
        jvm_set.append(java_home).append(" ");

        String log4j_path = jvm_j.getString(0) + version.getVersionLog4j2() + " ";
        jvm_set.append(log4j_path);

//        if (userxmx != null) {
//            xmx = userxmx;
//        }

        String Xmx = jvm_j.getString(1) + xmx + "M ";
        jvm_set.append(Xmx);

        String client_jar_path = jvm_j.getString(2) + version.getVersionJar() + " ";
        jvm_set.append(client_jar_path);

        String run = jvm_j.getString(3) + " ";
        jvm_set.append(run);


        String recovery = jvm_j.getString(4) + " ";
        jvm_set.append(recovery);


        Files.createDirectories(Paths.get(version.getVersionNativesPath()));
        String lib_path = jvm_j.getString(5) + version.getVersionNativesPath() + " ";
        jvm_set.append(lib_path);


        String starter = jvm_j.getString(6) + " -cp ";
        jvm_set.append(starter);

        version.setJvmattribute(jvm_set);
    }
}
