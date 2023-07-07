package org.wdt.game;

import org.wdt.download.fabric.FabricDownloadTask;
import org.wdt.download.forge.ForgeDownloadTask;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.Utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModList {

    public static Launcher getModTask(Launcher launcher) throws IOException {
        Pattern r = Pattern.compile("(.+)-(.+)-(.+)");
        Matcher m = r.matcher(Utils.getJSONObject(launcher.getVersionJson()).getString("id"));
        if (m.find()) {
            if (m.group(2).equals("forge")) {
                launcher.setForgeModDownloadTask(new ForgeDownloadTask(launcher, m.group(3)));
            } else if (m.group(2).equals("fabric")) {
                launcher.setFabricModDownloadTask(new FabricDownloadTask(m.group(3), launcher));
            }
        }
        return launcher;
    }

    public static void putGameId(Launcher launcher) throws IOException {
        JSONObject VersionJSONObject = Utils.getJSONObject(launcher.getVersionJson());
        if (launcher.getKind() == ModList.KindOfMod.FORGE) {
            JSONObject.PutKetToFile(launcher.getVersionJson(), VersionJSONObject, "id",
                    launcher.getVersion() + "-forge-" + launcher.getForgeDownloadTask().getForgeVersion());
        } else if (launcher.getKind() == ModList.KindOfMod.FABRIC) {
            JSONObject.PutKetToFile(launcher.getVersionJson(), VersionJSONObject, "id",
                    launcher.getVersion() + "-fabric-" + launcher.getFabricModDownloadTask().getFabricVersionNumber());
        }
    }

    public static boolean GameModIsForge(Launcher launcher) {
        return launcher.getKind() == KindOfMod.FORGE;
    }

    public static boolean GameModIsFabric(Launcher launcher) {
        return launcher.getKind() == KindOfMod.FABRIC;
    }

    public enum KindOfMod {
        Original,
        FABRIC,
        FABRICAPI,
        FORGE
    }

}
