package org.wdt.wdtc.download.fabric;

import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.FileUrl;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;

public class FabricDownloadTask extends FabricFileList {
    private final Logger logger = Logger.getLogger(getClass());
    private final String FabricMavenUrl = "https://maven.fabricmc.net/";
    private final String BmclMavenUrl = FileUrl.getBmclapiLibraries();
    private final Launcher launcher;
    private FabricAPIDownloadTask APIDownloadTask = null;

    public FabricDownloadTask(String FabricVersionNumber, String GameVersionNumber) {
        super(FabricVersionNumber, GameVersionNumber);
        this.launcher = new Launcher(getFabricVersionNumber());
    }

    public FabricDownloadTask(String FabricVersionNumber, Launcher launcher) {
        super(FabricVersionNumber, launcher);
        this.launcher = launcher;
    }

    public String FabricMaven() {
        if (launcher.bmclapi()) {
            return BmclMavenUrl;
        } else {
            return FabricMavenUrl;
        }
    }

    public void DownloadFile() throws IOException {
        for (String name : getFabricFileName()) {
            DependencyDownload dependency = new DependencyDownload(name);
            dependency.setDefaultUrl(FabricMaven());
            dependency.setDownloadPath(launcher.GetGameLibraryPath());
            try {
                DownloadTask.StartDownloadTask(dependency.getLibraryUrl(), dependency.getLibraryFile());
            } catch (IOException e) {
                logger.warn(e);
                dependency.setDefaultUrl(BmclMavenUrl);
                DownloadTask.StartDownloadTask(dependency.getLibraryUrl(), dependency.getLibraryFile());
            }
        }
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