package org.wdt.wdtc.download.quilt;

import org.wdt.platform.DependencyDownload;
import org.wdt.wdtc.game.Launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuiltLaunchTask {
    private final Launcher launcher;

    public QuiltLaunchTask(Launcher launcher) {
        this.launcher = launcher;
    }

    public List<String> LibraryList() throws IOException {
        List<String> List = new ArrayList<>();
        for (Map<String, String> map : launcher.getQuiltModDownloadTask().LibraryList()) {
            DependencyDownload download = new DependencyDownload(map.get("name"));
            download.setDownloadPath(launcher.GetGameLibraryPath());
            List.add(download.getLibraryFilePath());
        }
        return List;
    }
}
