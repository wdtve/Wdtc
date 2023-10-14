package org.wdt.wdtc.download.quilt;

import lombok.Getter;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.gson.JSONObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;

public class QuiltDownloadInfo implements DownloadInfo {
    private static final String LibraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s/profile/json";

    protected final Launcher launcher;
    @Getter
    protected final String QuiltVersionNumber;

    public QuiltDownloadInfo(Launcher launcher, String quiltVersionNumber) {
        this.launcher = launcher;
        QuiltVersionNumber = quiltVersionNumber;
    }

    public File getQuiltVersionJson() {
        return new File(FileManger.getWdtcCache() + "/" + launcher.getVersionNumber() + "-quilt-" + QuiltVersionNumber + ".json");
    }

    public String getQuiltVersionJsonUrl() {
        return String.format(LibraryListUrl, launcher.getVersionNumber(), QuiltVersionNumber);
    }

    public JSONObject getQuiltGameVersionJsonObject() throws IOException {
        return JSONUtils.getJSONObject(getQuiltVersionJson());
    }

    @Override
    public String getModVersion() {
        return QuiltVersionNumber;
    }

    @Override
    public InstallTask getModInstallTask() {
        return new QuiltInstallTask(launcher, QuiltVersionNumber);
    }
}
