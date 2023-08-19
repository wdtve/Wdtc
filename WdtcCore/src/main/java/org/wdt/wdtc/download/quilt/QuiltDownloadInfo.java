package org.wdt.wdtc.download.quilt;

import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.infterface.DownloadInfo;
import org.wdt.wdtc.download.infterface.InstallTask;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;

import java.io.File;
import java.io.IOException;

public class QuiltDownloadInfo implements DownloadInfo {
    private static final String LibraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s/profile/json";

    protected final Launcher launcher;
    protected final String QuiltVersionNumber;

    public QuiltDownloadInfo(Launcher launcher, String quiltVersionNumber) {
        this.launcher = launcher;
        QuiltVersionNumber = quiltVersionNumber;
    }

    public String getQuiltVersionNumber() {
        return QuiltVersionNumber;
    }

    public File getQuiltVersionJson() {
        return new File(FilePath.getWdtcCache() + "/" + launcher.getVersion() + "-quilt-" + QuiltVersionNumber + ".json");
    }

    public String getQuiltVersionJsonUrl() {
        return String.format(LibraryListUrl, launcher.getVersion(), QuiltVersionNumber);
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
