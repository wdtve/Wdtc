package org.WdtcLauncher.ClassPath;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcLauncher.ExtractFiles.ExtractFile;
import org.WdtcLauncher.FilePath;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ReadClass {
    private static final File m_t = new File(FilePath.getStarterBat());

    public static void readdown(String version_number) throws IOException {
        Version version = new Version(version_number);
        File lib_pay = new File(version.getVersionNativesPath());
        if (!lib_pay.exists()) {
            lib_pay.mkdirs();
        }
        StringBuilder cpb = new StringBuilder();
        JSONObject v_e_j = JSONObject.parseObject(FileUtils.readFileToString(new File(version.getVersionJson()), "UTF-8"));
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (natives_j == null) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (rules == null) {
                    cpb.append(readlib(lib_j, "path"));
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        cpb.append(readlib(lib_j, "path"));
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        cpb.append(readlib(lib_j, "path"));
                    }
                }


            } else {
                String natives_name = natives_j.getString("windows");
                if (natives_name != null) {
                    String natives_lib_path = readlib(lib_j, "path", natives_name);
                    ExtractFile.getExtractFiles(natives_lib_path, version.getVersionNativesPath());
                }
            }
        }
        String verions_jar = version.getVersionJar() + " ";
        String mainclass = v_e_j.getString("mainClass");
        cpb.append(verions_jar + mainclass);
        FileUtils.writeStringToFile(m_t, cpb.toString(), "UTF-8", true);
    }

    public static String readlib(JSONObject lib_j, String extract_content, String natives_name) {
        String game_lib_path = SetPath.getGame_lib_path();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString(extract_content);
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return natives_lib_path;
    }

    public static String readlib(JSONObject lib_j, String extract_content) {
        String game_lib_path = SetPath.getGame_lib_path();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString(extract_content) + ";";
        lib_path = lib_path.replaceAll("/", "\\\\");
        return lib_path;
    }
}
