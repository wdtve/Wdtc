package org.wdt.wdtc.download.fabric;


import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;

public class FabricDonwloadInfo {
    private static final Logger logmaker = WdtcLogger.getLogger(FabricDonwloadInfo.class);
    public final String FabricVersionNumber;
    public final Launcher launcher;
    private final DownloadSource source;
    private FabricAPIDownloadTask APIDownloadTask;


    public FabricDonwloadInfo(Launcher launcher, String FabricVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();
    }

    public String getFabricVersionNumber() {
        return FabricVersionNumber;
    }


    public String getFabricVersionFileUrl() {
        return Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/json";
    }


    public void DownloadProfileZip() {
        DownloadTask.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
    }

    public String getProfileZipFile() {
        return String.format(FilePath.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String getProfileZipUrl() {
        return String.format(Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/zip", launcher.getVersion(), getFabricVersionNumber());
    }

    public String FromFabricLoaderFolder() {
        return String.format("fabric-loader-%s-%s", getFabricVersionNumber(), launcher.getVersion());
    }

    public File getFabricJar() {
        return new File(FilePath.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".jar");
    }

    public File getFabricVersionJson() {
        return new File(String.format(FilePath.getWdtcCache() + "/%s-fabric-%s.json", launcher.getVersion(), FabricVersionNumber));
    }

    public JSONObject getFabricVersionJsonObject() throws IOException {
        return JSONUtils.getJSONObject(getFabricVersionJson());
    }


    public FabricInstallTask getFabricInstallTask() {
        return new FabricInstallTask(launcher, FabricVersionNumber);
    }

    public FabricAPIDownloadTask getAPIDownloadTask() {
        return APIDownloadTask;
    }

    public void setAPIDownloadTask(FabricAPIDownloadTask APIDownloadTask) {
        this.APIDownloadTask = APIDownloadTask;
    }

    public boolean getAPIDownloadTaskNoNull() {
        return APIDownloadTask != null;
    }
}
