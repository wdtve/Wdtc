package org.wdt.wdtc.game;

import org.apache.log4j.Logger;
import org.wdt.wdtc.download.fabric.FabricDonwloadInfo;
import org.wdt.wdtc.download.forge.ForgeDownloadInfo;
import org.wdt.wdtc.download.quilt.QuiltInstallTask;
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
                    case "forge" -> launcher.setForgeModDownloadInfo(new ForgeDownloadInfo(launcher, ModVersion));
                    case "fabric" -> launcher.setFabricModInstallInfo(new FabricDonwloadInfo(launcher, ModVersion));
                    case "quilt" -> launcher.setQuiltModDownloadInfo(new QuiltInstallTask(launcher, ModVersion));
                }
            }
            return launcher;
        } catch (IOException e) {
            logmaker.warn("", e);
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

    public enum KindOfMod {
        Original,
        FABRIC,
        FABRICAPI,
        FORGE,
        QUILT
    }

}
