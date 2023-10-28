package org.wdt.wdtc.core.download.fabric;

import lombok.Getter;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.URLUtils;
import org.wdt.wdtc.core.utils.gson.JSONArray;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.IOException;

public class FabricAPIDownloadTask {
    private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
    private final Launcher launcher;
    @Getter
    private final String FabricAPIVersionNumber;

    public FabricAPIDownloadTask(Launcher launcher, String FabricAPIVersionNumber) {
        this.launcher = launcher;
        this.FabricAPIVersionNumber = FabricAPIVersionNumber;
    }

    public void DownloadFabricAPI() throws IOException {
        JSONArray VersionListArray = JSONArray.parseJSONArray(URLUtils.getURLToString(VersionListUrl));
        for (int i = 0; i < VersionListArray.size(); i++) {
            JSONObject VersionObject = VersionListArray.getJSONObject(i);
            if (VersionObject.getString("version_number").equals(FabricAPIVersionNumber)) {
                String FabricAPIUrl = VersionObject.getJSONArray("files").getJSONObject(0).getString("url");
                String FabircAPIPath = launcher.getGameModsPath() + VersionObject.getJSONArray("files").getJSONObject(0).getString("filename");
                DownloadUtils.StartDownloadTask(FabricAPIUrl, FabircAPIPath);
            }
        }
    }

}
