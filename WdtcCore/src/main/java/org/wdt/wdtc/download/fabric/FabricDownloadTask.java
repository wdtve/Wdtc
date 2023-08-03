package org.wdt.wdtc.download.fabric;

import org.apache.log4j.Logger;
import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.download.DownloadTask;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;

public class FabricDownloadTask extends FabricFileList {
    private static final Logger logger = WdtcLogger.getLogger(FabricDownloadTask.class);
    private final Launcher launcher;
    private final DownloadSource source;
    private FabricAPIDownloadTask APIDownloadTask = null;


    public FabricDownloadTask(Launcher launcher, String FabricVersionNumber) {
        super(launcher, FabricVersionNumber);
        this.launcher = launcher;
        this.source = Launcher.getDownloadSource();

    }


    public void DownloadFile() throws IOException {
        for (String name : getFabricFileName()) {
            DependencyDownload dependency = new DependencyDownload(name);
            dependency.setDefaultUrl(source.getFabricLibraryUrl());
            dependency.setDownloadPath(launcher.GetGameLibraryPath());
            try {
                DownloadTask.StartDownloadTask(dependency.getLibraryUrl(), dependency.getLibraryFile());
            } catch (IOException e) {
                logger.warn(e);
                dependency.setDefaultUrl(Launcher.getOfficialDownloadSource().getFabricLibraryUrl());
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