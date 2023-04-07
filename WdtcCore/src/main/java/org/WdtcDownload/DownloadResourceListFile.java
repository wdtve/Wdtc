package org.WdtcDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class DownloadResourceListFile {
    private static File v_j;
    private static TextField label;
    private static boolean BMCLAPI = false;

    public DownloadResourceListFile(File v_j, TextField label, boolean BMCLAPI) {
        DownloadResourceListFile.v_j = v_j;
        DownloadResourceListFile.label = label;
        DownloadResourceListFile.BMCLAPI = BMCLAPI;
    }

    public void getresource_file() throws Exception {
        JSONObject v_e_j = JSON.parseObject(FileUtils.readFileToString(v_j, "UTF-8"));
        JSONObject assetIndex_j = v_e_j.getJSONObject("assetIndex");
        String id = assetIndex_j.getString("id");
        URL url = new URL(assetIndex_j.getString("url"));
        File game_assetsDir_j = new File(GetGamePath.getGameAssetsdir() + "indexes\\" + id + ".json");
        new WGet(url, game_assetsDir_j).download();
        DownloadGameResourceFile downloadGameResourceFile = new DownloadGameResourceFile(game_assetsDir_j, label, BMCLAPI);
        downloadGameResourceFile.gethash();

    }
}
