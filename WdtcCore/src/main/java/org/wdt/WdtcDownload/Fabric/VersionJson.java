package org.wdt.WdtcDownload.Fabric;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.wdt.Version;
import org.wdt.WdtcDownload.FileUrl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class VersionJson {
    private static final String FabricFileList = FileUrl.getFabricFileList();
    private static String FabricVersionNumber;
    private static String GameVersionNumber;
    private static File versionJson;


    public VersionJson(String FabricVersionNumber, Version version) {
        VersionJson.FabricVersionNumber = FabricVersionNumber;
        VersionJson.GameVersionNumber = version.getVersion();
        VersionJson.versionJson = new File(version.getVersionJson());
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
        VersionJson_json.put("libraries", JSON.toJSONString(LibraryList));
        String versionjsonend = JSON.toJSONString(VersionJson_json);
        FileUtils.writeStringToFile(versionJson, versionjsonend, "UTF-8");
    }
}
