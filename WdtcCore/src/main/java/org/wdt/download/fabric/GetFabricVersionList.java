package org.wdt.download.fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetFabricVersionList {
    private static final String FABRAIC_LIST = "https://meta.fabricmc.net/v2/versions/loader";
    public static List<String> getList() throws IOException {
        List<String> FabricVersionList = new ArrayList<>();
        URL FabricList = new URL(FABRAIC_LIST);
        URLConnection uc = FabricList.openConnection();
        JSONArray list = JSONArray.parseArray(IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8));
        for (int i = 0; i < list.size(); i++) {
            JSONObject fabricObject = list.getJSONObject(i);
            FabricVersionList.add(fabricObject.getString("version"));
        }
        return FabricVersionList;
    }
}
