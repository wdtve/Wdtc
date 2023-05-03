package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class DownloadResourceListFile extends DownloadTask {
    private static Launcher version;

    public DownloadResourceListFile(Launcher launcher) {
        super(launcher);
        DownloadResourceListFile.version = launcher;
    }

    public void GetresourceFile() throws IOException, InterruptedException {
        JSONObject assetIndex_j = StringUtil.FileToJSONObject(version.getVersionJson()).getJSONObject("assetIndex");
        String id = assetIndex_j.getString("id");
        URL url = new URL(assetIndex_j.getString("url"));
        File GameAssetsDirJson = new File(version.getGameAssetsdir() + "indexes\\" + id + ".json");
        FileUtils.copyURLToFile(url, GameAssetsDirJson);
        String list = StringUtil.FileToJSONObject(GameAssetsDirJson).getJSONObject("objects").values().toString();
        JSONArray l_e_j = JSON.parseArray(list);
        CountDownLatch countDownLatch = new CountDownLatch(l_e_j.size());
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            new Thread(StartDownloadHashTask(hash, countDownLatch)).start();
        }
        countDownLatch.await();
    }
}
