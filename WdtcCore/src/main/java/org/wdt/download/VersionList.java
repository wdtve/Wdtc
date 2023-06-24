package org.wdt.download;


import org.apache.log4j.Logger;
import org.wdt.platform.PlatformUtils;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VersionList {
    private static final Logger logmaker = Logger.getLogger(VersionList.class);


    public static List<String> getVersionList() {
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
