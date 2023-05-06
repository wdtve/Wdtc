package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.wdt.Launcher;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SelectGameVersion {
    private static final Logger LOGGER = Logger.getLogger(SelectGameVersion.class);
    private static TextField label = new TextField();
    private static final FileUrl fileUrl = new FileUrl();
    private static Launcher launcher;

    public SelectGameVersion(Launcher launcher, TextField label) {
        SelectGameVersion.label = label;
        SelectGameVersion.launcher = launcher;
    }

    public SelectGameVersion(Launcher launcher) {
        this(launcher, null);
    }

    public void selectversion() throws IOException, InterruptedException {
        JSONArray versions_j = JSONObject.parseObject(StringUtil.GetUrlContent(fileUrl.getVersionManifest())).getJSONArray("versions");
        for (int i = 0; i < versions_j.size(); i++) {
            String version_name = versions_j.getJSONObject(i).getString("id");
            if (Objects.equals(launcher.getVersion(), version_name)) {
                URL v_url = new URL(versions_j.getJSONObject(i).getString("url"));
                File v_j = new File(launcher.getVersionJson());
                DownloadTask.StartDownloadTask(v_url, v_j);
                new DownloadAndGameLibFile(launcher).readdown();
                if (Objects.nonNull(label)) {
                    label.setText("库下载完成");
                }
                LOGGER.debug("库下载完成");
                new DownloadResourceListFile(launcher).GetresourceFile();
                LOGGER.info("下载完成");
                if (Objects.nonNull(label)) {
                    label.setText("下载完成");
                }
            }
        }


    }

}
