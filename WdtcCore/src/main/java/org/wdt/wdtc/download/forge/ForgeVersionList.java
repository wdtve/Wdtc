package org.wdt.wdtc.download.forge;


import org.wdt.utils.gson.JSONArray;
import org.wdt.utils.gson.JSONObject;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForgeVersionList implements VersionList {
    private final Launcher launcher;


    public ForgeVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    public String getForgeListUrl() {
        return "https://bmclapi2.bangbang93.com/forge/minecraft/" + launcher.getVersionNumber();
    }

    @Override
    public List<String> getVersionList() throws IOException {
        List<String> VersionName = new ArrayList<>();
        JSONArray VersionList = JSONArray.parseJSONArray(PlatformUtils.UrltoString(getForgeListUrl()));
        for (int i = 0; i < VersionList.size(); i++) {
            JSONObject VersionObject = VersionList.getJSONObject(i);
            VersionName.add(VersionObject.getString("version"));
        }
        return VersionName;
    }
}
