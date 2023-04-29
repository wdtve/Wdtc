package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.control.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.wdt.StringUtil;
import org.wdt.Version;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class SelectGameVersion {
    private static final Logger LOGGER = Logger.getLogger(SelectGameVersion.class);
    private static String version_number;
    private static TextField label = new TextField();
    private static boolean BMCLAPI;
    private static Version version;
    private static FileUrl fileUrl;

    public SelectGameVersion(String version_number, TextField label, boolean BMCLAPI) {
        SelectGameVersion.version_number = version_number;
        SelectGameVersion.label = label;
        SelectGameVersion.BMCLAPI = BMCLAPI;
        SelectGameVersion.version = new Version(version_number);
        SelectGameVersion.fileUrl = new FileUrl(BMCLAPI);
    }


    public void selectversion() throws Exception {
        JSONArray versions_j = JSONObject.parseObject(StringUtil.GetUrlContent(fileUrl.getVersionManifest())).getJSONArray("versions");
        for (int i = 0; i < versions_j.size(); i++) {
            String version_name = versions_j.getJSONObject(i).getString("id");
            if (Objects.equals(version_number, version_name)) {
                URL v_url = new URL(versions_j.getJSONObject(i).getString("url"));
                File v_j = new File(version.getVersionJson());
                FileUtils.copyURLToFile(v_url, v_j);
                new DownloadAndGameLibFile(version_number, BMCLAPI).readdown();
                label.setText("库下载完成");
                LOGGER.debug("库下载完成");
                new DownloadResourceListFile(v_j, BMCLAPI).getresource_file();
                LOGGER.info("下载完成");
                label.setText("下载完成");
            }
        }


    }

}
