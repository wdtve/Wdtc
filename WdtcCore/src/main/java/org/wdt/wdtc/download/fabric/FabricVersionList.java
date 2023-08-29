package org.wdt.wdtc.download.fabric;


import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FabricVersionList implements VersionList {

    @Override
    public List<String> getVersionList() throws IOException {
        List<String> FabricVersionList = new ArrayList<>();
        JSONArray list = JSONArray.parseJSONArray(IOUtils.toString(Launcher.getDownloadSource().getFabricMetaUrl() + "v2/versions/loader"));
        for (int i = 0; i < list.size(); i++) {
            JSONObject fabricObject = list.getJSONObject(i);
            FabricVersionList.add(fabricObject.getString("version"));
        }
        return FabricVersionList;
    }
}
