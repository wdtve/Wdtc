package org.wdt.wdtc.download.game;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.UrlManger;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.download.infterface.VersionList;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameVersionList implements VersionList {
    private static final Logger logmaker = WdtcLogger.getLogger(GameVersionList.class);
    private final DownloadSource source;

    public GameVersionList() {
        this.source = UrlManger.DownloadSourceList.getDownloadSource();
    }


    public List<String> getVersionList() {
        List<String> VersionList = new ArrayList<>();
        try {
            JSONArray version_list = JSONObject.parseJSONObject(IOUtils.toString(source.getVersionManifestUrl())).getJSONArray("versions");
            for (int i = 0; i < version_list.size(); i++) {
                JSONObject VersionObject = version_list.getJSONObject(i);
                if (VersionObject.getString("type").equals("release")) {
                    VersionList.add(VersionObject.getString("id"));
                }
            }
        } catch (IOException e) {
            logmaker.error("* 出现错误,可能是网络错误", e);
        }
        return VersionList;
    }
}
