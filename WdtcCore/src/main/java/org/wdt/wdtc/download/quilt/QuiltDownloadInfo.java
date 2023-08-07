package org.wdt.wdtc.download.quilt;

import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.game.Launcher;

import java.io.File;
import java.io.IOException;

public class QuiltDownloadInfo {
    private static final String LibraryListUrl = "https://meta.quiltmc.org/v3/versions/loader/%s/%s/profile/json";

    public final Launcher launcher;
    public final String QuiltVersionNumber;

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

    public QuiltInstallTask getQuiltInstallTask() {
        return new QuiltInstallTask(launcher, QuiltVersionNumber);
    }
}
