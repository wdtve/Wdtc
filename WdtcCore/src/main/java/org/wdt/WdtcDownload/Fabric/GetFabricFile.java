package org.wdt.WdtcDownload.Fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.wdt.WdtcDownload.FileUrl;
import org.wdt.WdtcLauncher.Version;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
        URL FabricFileListUrl = new URL(String.format(FabricFileList, GameVersionNumber, FabricVersionNumber));
        URLConnection uc = FabricFileListUrl.openConnection();
        JSONObject FileList = JSONObject.parseObject(IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8));
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
