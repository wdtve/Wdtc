package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GetStartGameLibPath {
    private static final File m_t = FilePath.getStarterBat();
    private static Launcher launcher;

    public GetStartGameLibPath(Launcher launcher) {
        GetStartGameLibPath.launcher = launcher;
    }

    public void getLibPath() throws IOException {
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        StringBuilder cpb = new StringBuilder();
        JSONObject v_e_j = StringUtil.FileToJSONObject(launcher.getVersionJson());
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (Objects.isNull(natives_j)) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (Objects.isNull(rules)) {
                    cpb.append(readlib(lib_j));
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    JSONObject os_j = action_j.getJSONObject("os");
                    String os_n = os_j.getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        cpb.append(readlib(lib_j));
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        cpb.append(readlib(lib_j));
                    }
                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (Objects.nonNull(natives_name)) {
                    ExtractFile.unzipByFile(new File(readlib(lib_j, natives_name)), launcher.getVersionNativesPath());
                }
            }
        }
        String verions_jar = launcher.getVersionJar() + " ";
        String mainclass = v_e_j.getString("mainClass");
        cpb.append(verions_jar).append(mainclass);
        FileUtils.writeStringToFile(m_t, cpb.toString(), "UTF-8", true);
    }

    public static String readlib(JSONObject lib_j, String natives_name) {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString("path");
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return natives_lib_path;
    }

    public static String readlib(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path") + ";";
        lib_path = lib_path.replaceAll("/", "\\\\");
        return lib_path;
    }
}
