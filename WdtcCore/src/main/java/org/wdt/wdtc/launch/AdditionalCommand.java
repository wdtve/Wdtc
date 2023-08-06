package org.wdt.wdtc.launch;

import org.apache.commons.io.FilenameUtils;
import org.wdt.wdtc.game.Launcher;
import org.wdt.wdtc.game.ModList;

import java.io.IOException;
import java.util.List;

public class AdditionalCommand {
    public static String AdditionalGame(Launcher launcher) {
        if (ModList.GameModIsForge(launcher)) {
            return ListToString(launcher.getForgeDownloadTask().getForgeLaunchTask().getForgeLaunchGame(), " ");
        } else {
            return "";
        }
    }

    public static String AdditionalJvm(Launcher launcher) {
        if (ModList.GameModIsForge(launcher)) {
            return FilenameUtils.separatorsToUnix(ListToString(launcher.getForgeDownloadTask()
                    .getForgeLaunchTask().getForgeLuanchJvm(), " "));

        } else if (ModList.GameModIsFabric(launcher)) {
            return Space(launcher.getFabricModDownloadTask().getFabricLaunchTask().getJvm());
        } else {
            return "";
        }
    }

    public static String AdditionalLibrary(Launcher launcher) throws IOException {
        if (ModList.GameModIsForge(launcher)) {
            return ListToString(launcher.getForgeDownloadTask().getForgeLaunchTask().getForgeLaunchLibrary(), ";");
        } else if (ModList.GameModIsQuilt(launcher)) {
            return ListToString(launcher.getQuiltModDownloadTask().getQuiltLaunchTask().LibraryList(), ";");
        } else {
            return "";
        }
    }

    public static String GameMainClass(Launcher launcher) {
        if (ModList.GameModIsForge(launcher)) {
            return Space(launcher.getForgeDownloadTask().getForgeLaunchTask().getMainClass());
        } else if (ModList.GameModIsFabric(launcher)) {
            return Space("net.fabricmc.loader.impl.launch.knot.KnotClient");
        } else if (ModList.GameModIsQuilt(launcher)) {
            return Space("org.quiltmc.loader.impl.launch.knot.KnotClient");
        } else {
            return Space("net.minecraft.client.main.Main");
        }
    }

    private static String ListToString(List<String> list, String o) {
        StringBuilder List = new StringBuilder();
        for (String s : list) {
            List.append(s).append(o);
        }
        return new String(List);
    }

    public static String Space(String s) {
        return s + " ";
    }
}
