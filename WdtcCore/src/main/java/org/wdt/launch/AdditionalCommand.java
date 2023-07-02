package org.wdt.launch;

import org.apache.commons.io.FilenameUtils;
import org.wdt.game.Launcher;
import org.wdt.game.ModList;

import java.io.IOException;
import java.util.List;

public class AdditionalCommand {
    public static String AdditionalGame(Launcher launcher) throws IOException {
        if (ModList.GameModIsForge(launcher)) {
            return ListToString(launcher.getForgeDownloadTask().getForgeLaunchTask().getForgeLaunchGame(), " ");
        } else {
            return "";
        }
    }

    public static String AdditionalJvm(Launcher launcher) throws IOException {
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
        } else if (ModList.GameModIsFabric(launcher)) {
            return ListToString(launcher.getFabricModDownloadTask().getFabricLaunchTask().getFabricLibraryList(), ";");
        } else {
            return "";
        }
    }

    public static String GameMainClass(Launcher launcher) {
        if (ModList.GameModIsForge(launcher)) {
            return Space("cpw.mods.bootstraplauncher.BootstrapLauncher");
        } else if (ModList.GameModIsFabric(launcher)) {
            return Space(launcher.getFabricModDownloadTask().getFabricLaunchTask().getFabricMainClass());
        } else {
            return Space("net.minecraft.client.main.Main");
        }
    }

    private static String ListToString(List<String> list, String o) {
        StringBuilder List = new StringBuilder();
        for (String s : list) {
            List.append(s).append(o);
        }
        return List.toString();
    }

    public static String Space(String s) {
        return s + " ";
    }
}
