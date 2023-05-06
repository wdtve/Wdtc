package org.wdt.WdtcDownload;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VersionList {
    private static final Logger logmaker = Logger.getLogger(VersionList.class);
    private static final FileUrl fileUrl = new FileUrl();

    public List<String> getVersionList() {
        List<String> VersionList = new ArrayList<>();
        try {
            JSONArray version_list = JSONObject.parseObject(StringUtil.GetUrlContent(fileUrl.getVersionManifest())).getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                JSONObject VersionObject = version_list.getJSONObject(i);
                VersionList.add(VersionObject.getString("id"));
            }
        } catch (IOException e) {
            logmaker.error("* 出现错误,可能是网络错误", e);
        }
        return VersionList;
    }
}
