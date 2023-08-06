package org.wdt.wdtc.game;

import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.fabric.FabricInstallTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;
import org.wdt.wdtc.utils.WdtcLogger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModList {
    private static final Logger logmaker = WdtcLogger.getLogger(ModList.class);

    public static Launcher getModTask(Launcher launcher) {
        try {
            Pattern r = Pattern.compile("(.+?)-(.+?)-(.+)");
            Matcher m = r.matcher(launcher.getGameVersionJsonObject().getId());
            if (m.find()) {
                String ModName = m.group(2);
                String ModVersion = m.group(3);
                switch (ModName) {
                    case "forge" -> launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, ModVersion));
                    case "fabric" -> launcher.setFabricModDownloadTask(new FabricInstallTask(launcher, ModVersion));
                    case "quilt" -> launcher.setQuiltModDownloadTask(new QuiltDownloadTask(launcher, ModVersion));
                }
            }
            return launcher;
        } catch (IOException e) {
            logmaker.warn("", e);
            return null;
        }
    }

    public static void putGameId(Launcher launcher) {
        if (launcher.getKind() == KindOfMod.FORGE) {
            putGameId(launcher, "forge", launcher.getForgeDownloadTask().getForgeVersionNumber());
        } else if (launcher.getKind() == KindOfMod.FABRIC) {
            putGameId(launcher, "fabric", launcher.getFabricModDownloadTask().getFabricVersionNumber());
        } else if (launcher.getKind() == KindOfMod.QUILT) {
            putGameId(launcher, "quilt", launcher.getQuiltModDownloadTask().getQuiltVersionNumber());
        }
    }

    public static void putGameId(Launcher launcher, String kind, String ModVersionNumber) {
        try {
            GameVersionJsonObject gameVersionJsonObject = launcher.getGameVersionJsonObject();
            gameVersionJsonObject.setId(launcher.getVersion() + "-" + kind + "-" + ModVersionNumber);
            JSONUtils.ObjectToJsonFile(launcher.getVersionJson(), gameVersionJsonObject);
        } catch (IOException e) {
            logmaker.error("* Put Mod Kind Error,", e);
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

    public enum KindOfMod {
        Original,
        FABRIC,
        FABRICAPI,
        FORGE,
        QUILT
    }

}
