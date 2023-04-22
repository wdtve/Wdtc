package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VersionList {
    private static final Logger logmaker = Logger.getLogger(VersionList.class);
    private static FileUrl fileUrl;

    public VersionList(boolean bmcl) {
        fileUrl = new FileUrl(bmcl);
    }

    public List<String> getVersionList() {
        List<String> VersionList = new ArrayList<>();
        try {
            URL version_manifest_url = new URL(fileUrl.getVersionManifest());
            URLConnection uc = version_manifest_url.openConnection();
            String vm_e = IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
            JSONObject vm_e_j = JSONObject.parseObject(vm_e);
            JSONArray version_list = vm_e_j.getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                JSONObject VersionObject = version_list.getJSONObject(i);
                VersionList.add(VersionObject.getString("id"));
            }
        } catch (IOException e) {
            logmaker.error("* 出现错误,可能是网络错误");
        }
        return VersionList;
    }
}
