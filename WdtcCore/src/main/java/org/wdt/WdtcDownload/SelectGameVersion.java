package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.WdtcLauncher.Version;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
        String vm_e = "";
        try {
            URL version_manifest_url = new URL(fileUrl.getVersionManifest());
            URLConnection uc = version_manifest_url.openConnection();
            vm_e = IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("* 当前无网络");
        }
        JSONObject vm_e_j = JSONObject.parseObject(vm_e);
        JSONArray versions_j = vm_e_j.getJSONArray("versions");
        for (int i = 0; i < versions_j.size(); i++) {
            String version_name = versions_j.getJSONObject(i).getString("id");
            if (Objects.equals(version_number, version_name)) {
                URL v_url = new URL(versions_j.getJSONObject(i).getString("url"));
                File v_j = new File(version.getVersionJson());
                new WGet(v_url, v_j).download();
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
