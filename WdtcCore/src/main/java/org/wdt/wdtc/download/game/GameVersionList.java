package org.wdt.wdtc.download.game;


import org.apache.log4j.Logger;
import org.wdt.utils.gson.JSONArray;
import org.wdt.utils.gson.JSONObject;
import org.wdt.utils.gson.JSONUtils;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameVersionList implements VersionList {
    private static final Logger logmaker = WdtcLogger.getLogger(GameVersionList.class);

    public GameVersionList() {
    }


    public List<String> getVersionList() {
        List<String> VersionList = new ArrayList<>();
        try {
            JSONArray version_list = JSONUtils.getJSONObject(FileManger.getVersionManifestFile()).getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                JSONObject VersionObject = version_list.getJSONObject(i);
                if (VersionObject.getString("type").equals("release")) {
                    VersionList.add(VersionObject.getString("id"));
                }
            }
        } catch (IOException e) {
            logmaker.error(WdtcLogger.getErrorMessage(e));
        }
        return VersionList;
    }
}
