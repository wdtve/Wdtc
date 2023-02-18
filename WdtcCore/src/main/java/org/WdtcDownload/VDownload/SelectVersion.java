package org.WdtcDownload.VDownload;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.axet.wget.WGet;
import javafx.scene.control.TextField;
import org.WdtcDownload.DownloadLib.GetLibUrl;
import org.WdtcDownload.ResourceFile.DownloadResourceFile;
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
    private static final File vm_j = new File("WdtcCore/ResourceFile/Download/version_manifest.json");
    private static final Logger LOGGER = Logger.getLogger(SelectVersion.class);
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static String v;
    private static String v_path;
    private static TextField label = new TextField();
    private static boolean BMCLAPI;

    public SelectVersion(String v, String v_path, TextField label, boolean BMCLAPI) {
        SelectVersion.v = v;
        SelectVersion.v_path = v_path;
        SelectVersion.label = label;
        SelectVersion.BMCLAPI = BMCLAPI;
    }

    public SelectVersion(String v, String v_path) {
        this(v, v_path, label, BMCLAPI);
    }

    public void select_v() throws IOException, InterruptedException {
        String f_u_e = FileUtils.readFileToString(f_u);
        JSONObject f_u_j = JSON.parseObject(f_u_e);
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
                String s_e = FileUtils.readFileToString(s_j);
                JSONObject s_e_j = JSON.parseObject(s_e);
                File v_j = new File(s_e_j.getString("v_lib_path") + v + "\\" + v + ".json");
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
