package org.wdt.WdtcDownload.Fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.wdt.StringUtil;
import org.wdt.Version;
import org.wdt.WdtcDownload.FileUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFabricFile {
    private static final String FabricFileList = FileUrl.getFabricFileList();
    private static String FabricVersionNumber;
    private static String GameVersionNumber;

    public GetFabricFile(String FabricVersionNumber, String GameVersionNumber) {
        GetFabricFile.FabricVersionNumber = FabricVersionNumber;
        GetFabricFile.GameVersionNumber = GameVersionNumber;
    }

    public GetFabricFile(String FabricVersionNumber, Version version) {
        GetFabricFile.FabricVersionNumber = FabricVersionNumber;
        GetFabricFile.GameVersionNumber = version.getVersion();
    }

    public List<String> getFabricFileName() throws IOException {
        List<String> StringFileList = new ArrayList<>();
        JSONObject FileList = JSONObject.parseObject(StringUtil.GetUrlContent(String.format(FabricFileList, GameVersionNumber, FabricVersionNumber)));
        JSONObject loader = FileList.getJSONObject("loader");
        StringFileList.add(loader.getString("maven"));
        JSONObject intermediary = FileList.getJSONObject("intermediary");
        StringFileList.add(intermediary.getString("maven"));
        JSONArray common = FileList.getJSONObject("launcherMeta").getJSONObject("libraries").getJSONArray("common");
        for (int i = 0; i < common.size(); i++) {
            JSONObject FabricList = common.getJSONObject(i);
            StringFileList.add(FabricList.getString("name"));
        }
        return StringFileList;
    }
}
