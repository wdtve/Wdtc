package org.wdt.wdtc.game;


import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.launch.GetGamePath;

import java.io.IOException;

public class Version extends GetGamePath {
    private final String version;

    public Version(String version) {
        this.version = version;
    }

    public Version(String version, String here) {
        super(here);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }


    public String getVersionPath() {
        return getGameVersionPath() + version + "/";
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
        JSONObject AssetIndexJson = JSONUtils.getJSONObject(getVersionJson()).getJSONObject("assetIndex");
        String id = AssetIndexJson.getString("id");
        return getGameAssetsdir() + "indexes/" + id + ".json";
    }

    public String getGameOptionsFile() {
        return getVersionPath() + "options.txt";
    }

    public String getGameModsPath() {
        return getVersionPath() + "mods/";
    }

    public String getGameLogDir() {
        return getVersionPath() + "logs/";
    }

}
