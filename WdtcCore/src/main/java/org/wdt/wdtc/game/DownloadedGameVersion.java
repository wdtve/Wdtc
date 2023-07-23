package org.wdt.wdtc.game;

import org.wdt.wdtc.launch.GetGamePath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadedGameVersion {

    public static List<Launcher> getGameVersionList(GetGamePath path) {
        List<Launcher> GameVersionList = new ArrayList<>();
        File[] VersionList = new File(path.getGameVersionPath()).listFiles();
        if (VersionList != null && VersionList.length != 0) {
            for (File VersionFolder : VersionList) {
                GameVersionList.add(new Launcher(VersionFolder.getName()));
            }
            return GameVersionList;
        } else {
            return null;
        }
    }
}
