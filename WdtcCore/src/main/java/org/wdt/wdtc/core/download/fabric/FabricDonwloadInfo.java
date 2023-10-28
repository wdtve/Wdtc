package org.wdt.wdtc.core.download.fabric;


import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.download.infterface.DownloadInfoInterface;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.game.Launcher;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.DownloadUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONObject;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;

public class FabricDonwloadInfo implements DownloadInfoInterface {
    private static final Logger logmaker = WdtcLogger.getLogger(FabricDonwloadInfo.class);
    @Getter
    protected final String FabricVersionNumber;
    protected final Launcher launcher;
    @Getter
    @Setter
    private FabricAPIDownloadTask APIDownloadTask;


    public FabricDonwloadInfo(Launcher launcher, String FabricVersionNumber) {
        this.FabricVersionNumber = FabricVersionNumber;
        this.launcher = launcher;
    }


    public String getFabricVersionFileUrl() {
        return Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/json";
    }


    public void DownloadProfileZip() {
        DownloadUtils.StartDownloadTask(getProfileZipUrl(), getProfileZipFile());
    }

    public String getProfileZipFile() {
        return String.format(FileManger.getWdtcCache() + "/%s-%s-frofile-zip.zip", launcher.getVersionNumber(), getFabricVersionNumber());
    }

    public String getProfileZipUrl() {
        return String.format(Launcher.getOfficialDownloadSource().getFabricMetaUrl() + "v2/versions/loader/%s/%s/profile/zip", launcher.getVersionNumber(), getFabricVersionNumber());
    }

    public String FromFabricLoaderFolder() {
        return String.format("fabric-loader-%s-%s", getFabricVersionNumber(), launcher.getVersionNumber());
    }

    public File getFabricJar() {
        return new File(FileManger.getWdtcCache() + "/" + FromFabricLoaderFolder() + "/" + FromFabricLoaderFolder() + ".jar");
    }

    public File getFabricVersionJson() {
        return new File(String.format(FileManger.getWdtcCache() + "/%s-fabric-%s.json", launcher.getVersionNumber(), FabricVersionNumber));
    }

    public JSONObject getFabricVersionJsonObject() throws IOException {
        return JSONUtils.readJsonFiletoJSONObject(getFabricVersionJson());
    }



    public boolean getAPIDownloadTaskNoNull() {
        return APIDownloadTask != null;
    }

    @Override
    public String getModVersion() {
        return FabricVersionNumber;
    }

    @Override
    public InstallTaskInterface getModInstallTask() {
        return new FabricInstallTask(launcher, FabricVersionNumber);
    }
}
