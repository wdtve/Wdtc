package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.launch.GetGamePath;
import org.wdt.platform.PlatformUtils;

import java.io.IOException;

public class Version extends GetGamePath {
    private static String version;

    public Version(String version) {
        Version.version = version;
    }

    public Version(String version, String here) {
        super(here);
        Version.version = version;
    }

    public String getVersion() {
        return version;
    }



    public String getVersionPath() {
        return getGameVersionPath() + version + "\\";
    }

    public String getVersionJson() {
        return getVersionPath() + version + ".json";
    }

    public String getVersionJar() {
        return getVersionPath() + version + ".jar";
    }

    public String getVersionLog4j2() {
        return getVersionPath() + "log4j2.xml";
    }

    public String getVersionNativesPath() {
        return getVersionPath() + "natives-windows-x86_64";
    }

    public String getGameAssetsListJson() throws IOException {
        JSONObject AssetIndexJson = PlatformUtils.FileToJSONObject(getVersionJson()).getJSONObject("assetIndex");
        String id = AssetIndexJson.getString("id");
        return getGameAssetsdir() + "indexes\\" + id + ".json";
    }

    public String getGameOptionsFile() {
        return getGamePath() + "options.txt";
    }
}
