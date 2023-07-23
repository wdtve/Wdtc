package org.wdt.wdtc.game;

import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModList {

    public static Launcher getModTask(Launcher launcher) {
        try {
            Pattern r = Pattern.compile("(.+?)-(.+?)-(.+)");
            Matcher m = r.matcher(JSONUtils.getJSONObject(launcher.getVersionJson()).getString("id"));
            if (m.find()) {
                String ModName = m.group(2);
                String ModVersion = m.group(3);
                switch (ModName) {
                    case "forge" -> launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, ModVersion));
                    case "fabric" -> launcher.setFabricModDownloadTask(new FabricDownloadTask(ModVersion, launcher));
                    case "quilt" -> launcher.setQuiltModDownloadTask(new QuiltDownloadTask(launcher, ModVersion));
                }
            }
            return launcher;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putGameId(Launcher launcher) throws IOException {
        JSONObject VersionJSONObject = JSONUtils.getJSONObject(launcher.getVersionJson());
        if (launcher.getKind() == KindOfMod.FORGE) {
            JSONObject.PutKeyToFile(launcher.getVersionJson(), VersionJSONObject, "id",
                    launcher.getVersion() + "-forge-" + launcher.getForgeDownloadTask().getForgeVersion());
        } else if (launcher.getKind() == KindOfMod.FABRIC) {
            JSONObject.PutKeyToFile(launcher.getVersionJson(), VersionJSONObject, "id",
                    launcher.getVersion() + "-fabric-" + launcher.getFabricModDownloadTask().getFabricVersionNumber());
        } else if (launcher.getKind() == KindOfMod.QUILT) {
            JSONObject.PutKeyToFile(launcher.getVersionJson(), VersionJSONObject, "id",
                    launcher.getVersion() + "-quilt-" + launcher.getQuiltModDownloadTask().getQuiltVersionNumber());
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
