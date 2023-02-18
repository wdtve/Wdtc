package org.WdtcDownload.ResourceFile;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class GetFileList {
    public static void getFileList(File game_dir, TextField label, boolean BMCL) throws IOException, InterruptedException {
        String a_e = FileUtils.readFileToString(game_dir);
        JSONObject a_e_j = JSON.parseObject(a_e);
        JSONObject object_j = a_e_j.getJSONObject("objects");
        String file_list = object_j.values().toString();
        new GetHash(file_list, BMCL, label).gethash();
    }
}
