package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DownloadGameResourceFile extends DownloadTask {
    private static String list;

    public DownloadGameResourceFile(File game_dir, boolean BMCL) throws IOException {
        super(BMCL);
        DownloadGameResourceFile.list = StringUtil.FileToJSONObject(game_dir).getJSONObject("objects").values().toString();
    }

    public void gethash() throws Exception {
        JSONArray l_e_j = JSON.parseArray(list);
        CountDownLatch countDownLatch = new CountDownLatch(l_e_j.size());
        for (int i = 0; i < l_e_j.size(); i++) {
            String hash = l_e_j.getJSONObject(i).getString("hash");
            new Thread(StartDownloadHashTask(hash, countDownLatch)).start();

        }
        countDownLatch.await();
    }
}

