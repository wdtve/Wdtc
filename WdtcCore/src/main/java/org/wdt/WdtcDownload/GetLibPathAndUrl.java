package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GetLibPathAndUrl {
    private static final String BMCLAPI_Libraries = FileUrl.getBmclapiLibraries();
    private static final String MOJANG_Libraries = FileUrl.getMojangLibraries();
    private static final Logger log = Logger.getLogger(GetLibPathAndUrl.class);
    private static boolean BMCLAPI;
    private static Launcher launcher;


    public GetLibPathAndUrl(Launcher launcher) {
        try {
            GetLibPathAndUrl.launcher = launcher;
            GetLibPathAndUrl.BMCLAPI = launcher.bmclapi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File readnatives_lib(JSONObject lib_j) throws IOException {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject natives_j = lib_j.getJSONObject("natives");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        String natives_name = natives_j.getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_path = game_lib_path + natives_os.getString("path");
        natives_lib_path = natives_lib_path.replaceAll("/", "\\\\");
        return new File(natives_lib_path);
    }

    public static URL readnatives_url(JSONObject lib_j) throws IOException {
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject natives_j = lib_j.getJSONObject("natives");
        JSONObject classifiers_j = downloads_j.getJSONObject("classifiers");
        String natives_name = natives_j.getString("windows");
        JSONObject natives_os = classifiers_j.getJSONObject(natives_name);
        String natives_lib_url = natives_os.getString("url");
        if (BMCLAPI) {
            natives_lib_url = natives_lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(natives_lib_url);
        } else {
            return new URL(natives_lib_url);
        }
    }

    public static File readlib_path(JSONObject lib_j) throws IOException {
        String game_lib_path = launcher.GetGameLibPath();
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_path = game_lib_path + artifact_j.getString("path");
        lib_path = lib_path.replaceAll("/", "\\\\");
        return new File(lib_path);

    }

    public static URL readlib_url(JSONObject lib_j) throws IOException {
        JSONObject downloads_j = lib_j.getJSONObject("downloads");
        JSONObject artifact_j = downloads_j.getJSONObject("artifact");
        String lib_url = artifact_j.getString("url");
        if (BMCLAPI) {
            lib_url = lib_url.replaceAll(MOJANG_Libraries, BMCLAPI_Libraries);
            return new URL(lib_url);
        } else {
            return new URL(lib_url);
        }
    }

}
