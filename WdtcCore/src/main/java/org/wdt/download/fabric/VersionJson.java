package org.wdt.download.fabric;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.wdt.Version;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class VersionJson {
    private static final String FabricFileList = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private final String FabricVersionNumber;
    private final String GameVersionNumber;
    private final File versionJson;


    public VersionJson(String FabricVersionNumber, Version version) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.GameVersionNumber = version.getVersion();
        this.versionJson = new File(version.getVersionJson());
    }

    public void modify() throws IOException {
        JSONObject VersionJson_json = JSONObject.parseObject(FileUtils.readFileToString(versionJson, "UTF-8"));
        URL FabricFileListUrl = new URL(String.format(FabricFileList, GameVersionNumber, FabricVersionNumber));
        URLConnection uc = FabricFileListUrl.openConnection();
        JSONObject FileList = JSONObject.parseObject(IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8));
        JSONArray common = FileList.getJSONObject("launcherMeta").getJSONObject("libraries").getJSONArray("common");
        JSONArray LibraryList = VersionJson_json.getJSONArray("libraries");
        for (int i = 0; i < common.size(); i++) {
            LibraryList.add(common.getJSONObject(i));
        }
        PlatformUtils.PutKeyToFile(versionJson, VersionJson_json, "libraries", LibraryList);
    }
}
