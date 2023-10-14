package org.wdt.wdtc.game;

import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.manger.GameFolderManger;
import org.wdt.wdtc.utils.ModUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadedGameVersion {
    public static List<Launcher> getGameVersionList(GameFolderManger path) {
        List<Launcher> GameVersionList = new ArrayList<>();
        File[] VersionList = path.getGameVersionsPath().listFiles();
        if (VersionList != null && VersionList.length != 0) {
            for (File VersionFolder : VersionList) {
                Launcher launcher = new Launcher(VersionFolder.getName());
                if (FileUtils.isFileExists(launcher.getVersionJson())) {
                    GameVersionList.add(ModUtils.getModTask(launcher));
                }
            }
            return GameVersionList;
        } else {
            return null;
        }
    }

    public static boolean isDownloadedGame(GameFolderManger path) {
        List<Launcher> list = getGameVersionList(path);
        if (list != null) {
            return !list.isEmpty();
        }
        return false;
    }
}