package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.StringUtil;

import java.io.File;
import java.net.URL;

public class DownloadResourceListFile {
    private static File v_j;
    private static boolean BMCLAPI = false;

    public DownloadResourceListFile(File v_j, boolean BMCLAPI) {
        DownloadResourceListFile.v_j = v_j;
        DownloadResourceListFile.BMCLAPI = BMCLAPI;
    }

    public void getresource_file() throws Exception {
        JSONObject assetIndex_j = StringUtil.FileToJSONObject(v_j).getJSONObject("assetIndex");
        String id = assetIndex_j.getString("id");
        URL url = new URL(assetIndex_j.getString("url"));
        File game_assetsDir_j = new File(GetGamePath.getGameAssetsdir() + "indexes\\" + id + ".json");
        FileUtils.copyURLToFile(url, game_assetsDir_j);
        DownloadGameResourceFile downloadGameResourceFile = new DownloadGameResourceFile(game_assetsDir_j, BMCLAPI);
        downloadGameResourceFile.gethash();

    }
}
