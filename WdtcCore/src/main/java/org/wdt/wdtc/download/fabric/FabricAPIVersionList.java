package org.wdt.wdtc.download.fabric;

import org.wdt.utils.gson.JSONArray;
import org.wdt.utils.gson.JSONObject;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricAPIVersionList implements VersionList {
    private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
    private final Launcher launcher;

    public FabricAPIVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public List<String> getVersionList() throws IOException {
        List<String> VersionList = new ArrayList<>();
        JSONArray VersionListArray = JSONArray.parseJSONArray(PlatformUtils.UrltoString(VersionListUrl));
        for (int i = 0; i < VersionListArray.size(); i++) {
            JSONObject VersionObject = VersionListArray.getJSONObject(i);
            JSONArray GameVersionsList = VersionObject.getJSONArray("game_versions");
            for (int j = 0; j < GameVersionsList.size(); j++) {
                if (launcher.getVersionNumber().equals(GameVersionsList.getString(j))) {
                    VersionList.add(VersionObject.getString("version_number"));
                }
            }
        }
        return VersionList;
    }
}
