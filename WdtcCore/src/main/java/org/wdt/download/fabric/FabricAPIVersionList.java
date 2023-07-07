package org.wdt.download.fabric;

import org.wdt.game.Launcher;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricAPIVersionList {
    private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
    private final Launcher launcher;

    public FabricAPIVersionList(Launcher launcher) {
        this.launcher = launcher;
    }

    public List<String> getFabricAPIVersionList() throws IOException {
        List<String> VersionList = new ArrayList<>();
        JSONArray VersionListArray = JSONArray.parseWdtArray(PlatformUtils.GetUrlContent(VersionListUrl));
        for (int i = 0; i < VersionListArray.size(); i++) {
            JSONObject VersionObject = VersionListArray.getJSONObject(i);
            JSONArray GameVersionsList = VersionObject.getJSONArray("game_versions");
            for (int j = 0; j < GameVersionsList.size(); j++) {
                if (launcher.getVersion().equals(GameVersionsList.getString(j))) {
                    VersionList.add(VersionObject.getString("version_number"));
                }
            }
        }
        return VersionList;
    }
}
