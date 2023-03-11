package org.WdtcDownload.DownloadResourceFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class GetFileList {
    public static void getFileList(File game_dir, TextField label, boolean BMCL) throws Exception {
        JSONObject a_e_j = JSON.parseObject(FileUtils.readFileToString(game_dir, "UTF-8"));
        JSONObject object_j = a_e_j.getJSONObject("objects");
        String file_list = object_j.values().toString();
        new GetHash(file_list, BMCL, label).gethash();
    }
}
