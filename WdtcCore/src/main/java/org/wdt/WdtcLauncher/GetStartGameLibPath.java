package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.wdt.AboutSetting;
import org.wdt.FilePath;
import org.wdt.Launcher;
import org.wdt.StringUtil;
import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GetStartGameLibPath {
    private static final StringBuilder cpb = new StringBuilder();
    private static Launcher launcher;

    public static void getLibPath(Launcher launcher) throws IOException {
        GetStartGameLibPath.launcher = launcher;
        Files.createDirectories(Paths.get(launcher.getVersionNativesPath()));
        JSONObject v_e_j = StringUtil.FileToJSONObject(launcher.getVersionJson());
        JSONArray libraries_j = v_e_j.getJSONArray("libraries");
        for (int i = 0; i < libraries_j.size(); i++) {
            JSONObject lib_j = libraries_j.getJSONObject(i);
            JSONObject natives_j = lib_j.getJSONObject("natives");
            if (Objects.isNull(natives_j)) {
                JSONArray rules = lib_j.getJSONArray("rules");
                if (Objects.isNull(rules)) {
                    cpb.append(GetPath(lib_j));
                } else {
                    JSONObject action_j = rules.getJSONObject(rules.size() - 1);
                    String action = action_j.getString("action");
                    String os_n = action_j.getJSONObject("os").getString("name");
                    if (Objects.equals(action, "disallow") && Objects.equals(os_n, "osx")) {
                        cpb.append(GetPath(lib_j));
                    } else if (Objects.equals(action, "allow") && Objects.equals(os_n, "windows")) {
                        cpb.append(GetPath(lib_j));
                    }
                }
            } else {
                String natives_name = natives_j.getString("windows");
                if (Objects.nonNull(natives_name)) {
                    ExtractFile.unzipByFile(GetNativesPath(lib_j), launcher.getVersionNativesPath());
                }
            }
        }
        String verions_jar = launcher.getVersionJar() + " ";
        String mainclass = v_e_j.getString("mainClass");
        cpb.append(verions_jar);
        if (AboutSetting.GetLlvmpipeSwitch()) {
            cpb.append(LlbmpipeLoader());
        }
        cpb.append(mainclass).append(" ");
        launcher.setLibrartattribute(cpb);
    }

    private static String LittleskinApi_AuthlibInjector() throws IOException {
        return "-javaagent:" + FilePath.getAuthlibInjector() + "=" + FileUrl.getLittleskinApi() + " -Dauthlibinjector.yggdrasil.prefetched=" +
                StringUtil.StringToBase64(StringUtil.GetUrlContent(FileUrl.getLittleskinApi())) + " ";
    }

    private static String LlbmpipeLoader() {
        return "-javaagent:" + FilePath.getLlbmpipeLoader() + " ";
    }

    public static String GetNativesPath(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject natives_os = lib_j.getJSONObject("downloads").getJSONObject("classifiers")
                .getJSONObject("windows");
        String natives_lib_path = game_lib_path + natives_os.getString("path");
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return natives_lib_path;
    }

    public static String GetPath(JSONObject lib_j) {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject artifact_j = lib_j.getJSONObject("downloads").getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path") + ";";
        lib_path = lib_path.replaceAll("/", "\\\\");
        return lib_path;
    }
}
