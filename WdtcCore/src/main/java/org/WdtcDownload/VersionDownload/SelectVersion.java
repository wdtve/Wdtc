package org.WdtcDownload.VersionDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.WdtcDownload.DownloadLib.GetLibUrl;
import org.WdtcDownload.ResourceFile.DownloadResourceFile;
import org.WdtcLauncher.Version;
import org.apache.commons.io.FileUtils;
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
    private static final File f_u = new File("WdtcCore/ResourceFile/Download/file_url.json");
    private static final Logger LOGGER = Logger.getLogger(SelectVersion.class);
    private static String v;
    private static String v_path;
    private static TextField label = new TextField();
    private static boolean BMCLAPI;
    private static Version version;

    public SelectVersion(String v, String v_path, TextField label, boolean BMCLAPI) {
        SelectVersion.v = v;
        SelectVersion.v_path = v_path;
        SelectVersion.label = label;
        SelectVersion.BMCLAPI = BMCLAPI;
        SelectVersion.version = new Version(v);
    }


    public void select_v() throws IOException, InterruptedException {
        JSONObject f_u_j = JSON.parseObject(FileUtils.readFileToString(f_u, "UTF-8"));
        JSONObject BMCLAPI_J = f_u_j.getJSONObject("BMCLAPI");
        String vm_e = "";
        try {
            if (BMCLAPI) {
                URL version_manifest_url = new URL(BMCLAPI_J.getString("version_manifest"));
                URLConnection uc = version_manifest_url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
                vm_e = in.readLine();
            } else {
                URL version_manifest_url = new URL(f_u_j.getString("version_manifest"));
                URLConnection uc = version_manifest_url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
                vm_e = in.readLine();
            }
        } catch (IOException e) {
            LOGGER.error("* 当前无网络");
        }
        JSONObject vm_e_j = JSONObject.parseObject(vm_e);
        JSONArray versions_j = vm_e_j.getJSONArray("versions");
        for (int i = 0; i < versions_j.size(); i++) {
            String v_n = versions_j.getJSONObject(i).getString("id");
            if (Objects.equals(v, v_n)) {
                URL v_url = new URL(versions_j.getJSONObject(i).getString("url"));
                File v_j = new File(version.getVersion_json());
                new WGet(v_url, v_j).download();
                new GetLibUrl(v_j, v_path, v, BMCLAPI, label).readdown();
                label.setText("库下载完成");
                LOGGER.debug("库下载完成");
                new DownloadResourceFile(v_j, label, BMCLAPI).getresource_file();
                LOGGER.debug("下载完成");
            }
        }


    }

}
