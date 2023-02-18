package org.WdtcLauncher.ClassPath;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.WdtcLauncher.ExtractFiles.ExtractFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ReadClass {
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static final File m_t = new File("WdtcCore/ResourceFile/Launcher/starter.bat");

    public static void readdown(File v_j, String v_path, String version) throws IOException {
        String library_p = v_path + "natives-windows-x86_64";
        File lib_pay = new File(library_p);
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
        }
        StringBuilder cpb = new StringBuilder();
        String v_e = FileUtils.readFileToString(v_j);
        JSONObject v_e_j = JSONObject.parseObject(v_e);
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (natives_j == null) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (rules == null) {
                    cpb.append(readlib(lib_j, "path", s_j));
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        cpb.append(readlib(lib_j, "path", s_j));
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        cpb.append(readlib(lib_j, "path", s_j));
                    }
                }


            } else {
                String natives_name = natives_j.getString("windows");
                if (natives_name != null) {
                    String natives_lib_path = readlib(lib_j, "path", s_j, natives_name);
                    ExtractFile.getExtractFiles(natives_lib_path, library_p);
                }
            }
        }
        String verions_jar = v_path + version + ".jar ";
        String mainclass = v_e_j.getString("mainClass");
        cpb.append(verions_jar + mainclass);
        FileUtils.writeStringToFile(m_t, cpb.toString(), true);
    }

    public static String readlib(JSONObject lib_j, String extract_content, File s_j, String natives_name) throws IOException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString(extract_content);
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return natives_lib_path;
    }

    public static String readlib(JSONObject lib_j, String extract_content, File s_j) throws IOException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_J = JSONObject.parseObject(s_e);
        String game_lib_path = s_e_J.getString("game_lib_path");
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString(extract_content) + ";";
        lib_path = lib_path.replaceAll("/", "\\\\");
        return lib_path;
    }
}
