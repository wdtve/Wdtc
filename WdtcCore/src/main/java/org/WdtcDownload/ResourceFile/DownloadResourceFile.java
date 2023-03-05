package org.WdtcDownload.ResourceFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.WdtcDownload.SetFilePath.SetPath;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadResourceFile {
    private static File v_j;
    private static TextField label;
    private static boolean BMCLAPI = false;

    public DownloadResourceFile(File v_j, TextField label, boolean BMCLAPI) {
        DownloadResourceFile.v_j = v_j;
        DownloadResourceFile.label = label;
        DownloadResourceFile.BMCLAPI = BMCLAPI;
    }

    public void getresource_file() throws IOException, InterruptedException {
        String v_e = FileUtils.readFileToString(v_j);
        JSONObject v_e_j = JSON.parseObject(v_e);
        JSONObject assetIndex_j = v_e_j.getJSONObject("assetIndex");
        String id = assetIndex_j.getString("id");
        URL url = new URL(assetIndex_j.getString("url"));
        File game_assetsDir_j = new File(SetPath.getGame_assetsdir() + "indexes\\" + id + ".json");
        new WGet(url, game_assetsDir_j).download();
        GetFileList.getFileList(game_assetsDir_j, label, BMCLAPI);

    }
}
