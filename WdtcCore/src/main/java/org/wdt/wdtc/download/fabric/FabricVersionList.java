package org.wdt.wdtc.download.fabric;


import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricVersionList {
    private static final String FABRAIC_LIST = "https://meta.fabricmc.net/v2/versions/loader";

    public static List<String> getList() throws IOException {
        List<String> FabricVersionList = new ArrayList<>();
        JSONArray list = JSONArray.parseWdtArray(PlatformUtils.GetUrlContent(FABRAIC_LIST));
        for (int i = 0; i < list.size(); i++) {
            JSONObject fabricObject = list.getJSONObject(i);
            FabricVersionList.add(fabricObject.getString("version"));
        }
        return FabricVersionList;
    }
}
