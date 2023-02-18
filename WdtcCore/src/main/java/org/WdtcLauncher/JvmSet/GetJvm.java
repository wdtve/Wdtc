package org.WdtcLauncher.JvmSet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GetJvm {

    private static final File u_f = new File("WdtcCore/ResourceFile/Launcher/launcher.json");
    private static final File m_t = new File("WdtcCore/ResourceFile/Launcher/starter.bat");
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");

    public static void read_jvm(String xmx, File v_j, String java_path, String v_path, String v) throws IOException {
        StringBuffer jvm_set = new StringBuffer();
        String v_j_e = FileUtils.readFileToString(v_j);
        JSONObject v_e_j = JSON.parseObject(v_j_e);
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_j = JSON.parseObject(s_e);
        String u_e = FileUtils.readFileToString(u_f);
        JSONObject u_j_e = JSON.parseObject(u_e);
        JSONObject jvmIndex_j = u_j_e.getJSONObject("jvmIndex");
        JSONArray jvm_j = u_j_e.getJSONArray("jvm");
        String game_p = s_e_j.getString("game_path");
        jvm_set.append("@echo off\ncd " + game_p + "\n");
        jvm_set.append(java_path + " ");

        String log4j_path = jvm_j.getString(0) + v_path + "log4j2.xml ";
        jvm_set.append(log4j_path);

        String Xmx = jvm_j.getString(1) + xmx + "M ";
        jvm_set.append(Xmx);

        String client_jar_path = jvm_j.getString(2) + v_path + v + ".jar ";
        jvm_set.append(client_jar_path);

        String run = jvm_j.getString(3) + " ";
        jvm_set.append(run);

        String recovery = jvm_j.getString(4) + " ";
        jvm_set.append(recovery);


        String library_p = v_path + "natives-windows-x86_64";
        File lib_pay = new File(library_p);
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
            String lib_path = jvm_j.getString(5) + library_p + " ";
            jvm_set.append(lib_path);
        } else if (lib_pay.exists()) {
            String lib_path = jvm_j.getString(5) + library_p + " ";
            jvm_set.append(lib_path);
        }

        String starter = jvm_j.getString(6) + " -cp ";
        jvm_set.append(starter);

        FileUtils.writeStringToFile(m_t, jvm_set.toString());
//        System.out.println(jvm_set);
    }
}
