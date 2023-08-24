package org.wdt.wdtc.game;

import org.wdt.wdtc.launch.GamePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadedGameVersion {

    public static List<Launcher> getGameVersionList(GamePath path) {
        List<Launcher> GameVersionList = new ArrayList<>();
        File[] VersionList = path.getGameVersionsPath().listFiles();
        if (VersionList != null && VersionList.length != 0) {
            for (File VersionFolder : VersionList) {
                Launcher launcher = new Launcher(VersionFolder.getName());
                try {
                    if (!PlatformUtils.FileExistenceAndSize(launcher.getVersionJson())) {
                        GameVersionList.add(ModUtils.getModTask(launcher));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return GameVersionList;
        } else {
            return null;
        }
    }

    public static boolean isDownloadedGame(GamePath path) {
        List<Launcher> list = getGameVersionList(path);
        if (list != null) {
            return !list.isEmpty();
        }
        return false;
    }
}