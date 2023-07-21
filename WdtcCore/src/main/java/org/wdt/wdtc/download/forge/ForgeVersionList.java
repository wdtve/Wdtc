package org.wdt.wdtc.download.forge;


import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForgeVersionList {
    private static final String BMCALAPI_FORGE_LIST = "https://bmclapi2.bangbang93.com/forge/minecraft/";
    private final Launcher launcher;


    public ForgeVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    public String getForgeListUrl() {
        return BMCALAPI_FORGE_LIST + launcher.getVersion();
    }

    public List<String> getForgeVersion() throws IOException {
        List<String> VersionName = new ArrayList<>();
        JSONArray VersionList = JSONArray.parseWdtArray(PlatformUtils.GetUrlContent(getForgeListUrl()));
        for (int i = 0; i < VersionList.size(); i++) {
            JSONObject VersionObject = VersionList.getJSONObject(i);
            VersionName.add(VersionObject.getString("version"));
        }
        return VersionName;
    }
}
