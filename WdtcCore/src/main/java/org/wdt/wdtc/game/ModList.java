package org.wdt.wdtc.game;

import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;
import org.wdt.wdtc.download.fabric.FabricDownloadTask;
import org.wdt.wdtc.download.forge.ForgeDownloadTask;
import org.wdt.wdtc.download.quilt.QuiltDownloadTask;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModList {

    public static void getModTask(Launcher launcher) throws IOException {
        Pattern r = Pattern.compile("(.+?)-(.+?)-(.+)");
        Matcher m = r.matcher(Utils.getJSONObject(launcher.getVersionJson()).getString("id"));
        if (m.find()) {
            String ModName = m.group(2);
            String ModVersion = m.group(3);
            switch (ModName) {
                case "forge" -> launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, ModVersion));
                case "fabric" -> launcher.setFabricModDownloadTask(new FabricDownloadTask(ModVersion, launcher));
                case "quilt" -> launcher.setQuiltModDownloadTask(new QuiltDownloadTask(launcher, ModVersion));
            }
        }
    }

    public static void putGameId(Launcher launcher) throws IOException {
        JSONObject VersionJSONObject = Utils.getJSONObject(launcher.getVersionJson());
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