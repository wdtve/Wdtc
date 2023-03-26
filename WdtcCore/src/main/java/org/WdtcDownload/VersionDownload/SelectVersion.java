package org.WdtcDownload.VersionDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.WdtcDownload.DownloadLib.GetLibUrl;
import org.WdtcDownload.DownloadResourceFile.DownloadResourceFile;
import org.WdtcDownload.FileUrl;
import org.WdtcLauncher.Version;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SelectVersion {
    private static final Logger LOGGER = Logger.getLogger(SelectVersion.class);
    private static String version_number;
    private static TextField label = new TextField();
    private static boolean BMCLAPI;
    private static Version version;
    private static FileUrl fileUrl;

    public SelectVersion(String version_number, TextField label, boolean BMCLAPI) {
        SelectVersion.version_number = version_number;
        SelectVersion.label = label;
        SelectVersion.BMCLAPI = BMCLAPI;
        SelectVersion.version = new Version(version_number);
        SelectVersion.fileUrl = new FileUrl(BMCLAPI);
    }


    public void selectversion() throws Exception {
        String vm_e = "";
        try {
            URL version_manifest_url = new URL(fileUrl.getVersionManifest());
            URLConnection uc = version_manifest_url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            vm_e = in.readLine();
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
                new GetLibUrl(version_number, BMCLAPI).readdown();
                label.setText("库下载完成");
                LOGGER.debug("库下载完成");
                new DownloadResourceFile(v_j, label, BMCLAPI).getresource_file();
                LOGGER.debug("下载完成");
            }
        }


    }

}
