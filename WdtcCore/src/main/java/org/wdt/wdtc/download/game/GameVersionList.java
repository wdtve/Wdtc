package org.wdt.wdtc.download.game;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.utils.PlatformUtils;
import org.wdt.wdtc.utils.getWdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameVersionList implements VersionList {
    private static final Logger logmaker = getWdtcLogger.getLogger(GameVersionList.class);


    public List<String> getVersionList() {
        List<String> VersionList = new ArrayList<>();
        try {
            JSONArray version_list = JSONObject.parseWdtObject(PlatformUtils.GetUrlContent(FileUrl.getVersionManifest())).getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                JSONObject VersionObject = version_list.getJSONObject(i);
                VersionList.add(VersionObject.getString("id"));
            }
        } catch (IOException e) {
            logmaker.error("* 出现错误,可能是网络错误", e);
        }
        return VersionList;
    }
}
