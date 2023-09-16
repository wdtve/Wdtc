package org.wdt.wdtc.download.fabric;

import org.wdt.utils.gson.JSONArray;
import org.wdt.utils.gson.JSONObject;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.IOException;

public class FabricAPIDownloadTask {
    private static final String VersionListUrl = "https://api.modrinth.com/v2/project/P7dR8mSH/version";
    private final Launcher launcher;
    private final String FabricAPIVersionNumber;

    public FabricAPIDownloadTask(Launcher launcher, String FabricAPIVersionNumber) {
        this.launcher = launcher;
        this.FabricAPIVersionNumber = FabricAPIVersionNumber;
    }

    public void DownloadFabricAPI() throws IOException {
        JSONArray VersionListArray = JSONArray.parseJSONArray(PlatformUtils.UrltoString(VersionListUrl));
        for (int i = 0; i < VersionListArray.size(); i++) {
            JSONObject VersionObject = VersionListArray.getJSONObject(i);
            if (VersionObject.getString("version_number").equals(FabricAPIVersionNumber)) {
                String FabricAPIUrl = VersionObject.getJSONArray("files").getJSONObject(0).getString("url");
                String FabircAPIPath = launcher.getGameModsPath() + VersionObject.getJSONArray("files").getJSONObject(0).getString("filename");
                DownloadTask.StartDownloadTask(FabricAPIUrl, FabircAPIPath);
            }
        }
    }

    public String getFabricAPIVersionNumber() {
        return FabricAPIVersionNumber;
    }
}
