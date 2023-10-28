package org.wdt.wdtc.core.utils;

import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.core.download.infterface.DownloadInfoInterface;
import org.wdt.wdtc.core.download.infterface.InstallTaskInterface;
import org.wdt.wdtc.core.download.quilt.QuiltInstallTask;
import org.wdt.wdtc.core.game.Launcher;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(ModUtils.class);

    public static Launcher getModTask(Launcher launcher) {
        try {
            if (FileUtils.isFileNotExists(launcher.getVersionJson())) {
                return null;
            }
            Pattern r = Pattern.compile("(.+?)-(.+?)-(.+)");
            Matcher m = r.matcher(launcher.getGameVersionJsonObject().getId());
            if (m.find()) {
                String ModName = m.group(2);
                String ModVersion = m.group(3);
                switch (ModName) {
                    case "forge" -> launcher.setForgeModDownloadInfo(new ForgeDownloadInfo(launcher, ModVersion));
                    case "fabric" -> launcher.setFabricModInstallInfo(new FabricDonwloadInfo(launcher, ModVersion));
                    case "quilt" -> launcher.setQuiltModDownloadInfo(new QuiltInstallTask(launcher, ModVersion));
                }
            }
            return launcher;
        } catch (IOException e) {
            logmaker.error(e);
            return null;
        }
    }


    public static boolean GameModIsForge(Launcher launcher) {
        return launcher.getKind() == KindOfMod.FORGE;
    }

    public static boolean GameModIsFabric(Launcher launcher) {
        return launcher.getKind() == KindOfMod.FABRIC;
    }

    public static boolean GameModIsQuilt(Launcher launcher) {
        return launcher.getKind() == KindOfMod.QUILT;
    }

    public static DownloadInfoInterface getModDownloadInfo(Launcher launcher) {
        if (GameModIsFabric(launcher)) {
            return launcher.getFabricModInstallInfo();
        } else if (GameModIsForge(launcher)) {
            return launcher.getForgeDownloadInfo();
        } else if (GameModIsQuilt(launcher)) {
            return launcher.getQuiltModDownloadInfo();
        } else {
            return null;
        }
    }

    public static DownloadInfoInterface getVersionModInstall(Launcher launcher, KindOfMod kind) {
        if (kind == KindOfMod.QUILT) {
            return launcher.getQuiltModDownloadInfo();
        } else if (kind == KindOfMod.FORGE) {
            return launcher.getForgeDownloadInfo();
        } else if (kind == KindOfMod.FABRIC) {
            return launcher.getFabricModInstallInfo();
        } else {
            return null;
        }
    }

    public static InstallTaskInterface getModInstallTask(Launcher launcher) {
        DownloadInfoInterface info = getModDownloadInfo(launcher);
        if (info != null) {
            return info.getModInstallTask();
        } else {
            return null;
        }
    }

    public enum KindOfMod {
        Original, FABRIC, FABRICAPI, FORGE, QUILT
    }

}
