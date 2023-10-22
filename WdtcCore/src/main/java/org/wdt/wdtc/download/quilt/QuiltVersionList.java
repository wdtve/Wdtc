package org.wdt.wdtc.download.quilt;

import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.URLUtils;
import org.wdt.wdtc.utils.gson.JSONArray;
import org.wdt.wdtc.utils.gson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuiltVersionList implements VersionList {
    private static final String QuiltVersionListUrl = "https://meta.quiltmc.org//v3/versions/loader/%s";
    private final Launcher launcher;

    public QuiltVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public List<String> getVersionList() throws IOException {
        List<String> List = new ArrayList<>();
        JSONArray VersionArray = JSONArray.parseJSONArray(URLUtils.getURLToString(String.format(QuiltVersionListUrl, launcher.getVersionNumber())));
        for (int i = 0; i < VersionArray.size(); i++) {
            JSONObject VersionObject = VersionArray.getJSONObject(i);
            List.add(VersionObject.getJSONObject("loader").getString("version"));
        }
        return List;
    }
}
