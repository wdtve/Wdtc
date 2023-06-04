package org.wdt.download.fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.wdt.Version;
import org.wdt.platform.PlatformUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFabricFile {
    private static final String FabricFileList = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private final String BMCLAPI_FABRIC_FILE_LIST = "https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader/%s/%s";
    private final String FabricVersionNumber;
    private final String GameVersionNumber;

    public GetFabricFile(String FabricVersionNumber, String GameVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.GameVersionNumber = GameVersionNumber;
    }

    public GetFabricFile(String FabricVersionNumber, Version version) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.GameVersionNumber = version.getVersion();
    }

    public List<String> getFabricFileName() throws IOException {
        List<String> StringFileList = new ArrayList<>();
        JSONObject FileList = JSONObject.parseObject(PlatformUtils.GetUrlContent(String.format(FabricFileList, GameVersionNumber, FabricVersionNumber)));
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
