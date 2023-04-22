package org.wdt.WdtcDownload.Fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetFabricVersionList {
    public static List<String> getList() throws IOException {
        List<String> FabricVersionList = new ArrayList<>();
        URL FabricList = new URL(FileUrl.getFabraicList());
        URLConnection uc = FabricList.openConnection();
        JSONArray list = JSONArray.parseArray(IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8));
        for (int i = 0; i < list.size(); i++) {
            JSONObject fabricObject = list.getJSONObject(i);
            FabricVersionList.add(fabricObject.getString("version"));
        }
        return FabricVersionList;
    }
}
