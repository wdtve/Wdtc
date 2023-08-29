package org.wdt.wdtc.platform;

import org.wdt.utils.FileUtils;

import java.io.File;

public class VMManger {
    private static final String LAUNCHER_VERSION = System.getProperty("launcher.version", "demo");
    private static final String LAUNCHER_AUTHOR = "Wdt~";
    private static final String OS = System.getProperty("os.name");
    private static final String client_id = System.getProperty("wtdc.oauth.clientId", "8c4a5ce9-55b9-442e-9bd0-17cf89689dd0");

    public static String getClient_id() {
        return client_id;
    }

    public static String getLauncherVersion() {
        return LAUNCHER_VERSION;
    }

    public static boolean getForgeSwitch() {
        return Boolean.getBoolean("download.forge");
    }

    public static File getWdtcConfigFromVM() {
        String WdtcConfigPath = System.getProperty("wdtc.config.path");
        if (WdtcConfigPath != null) {
            return new File(FileUtils.getCanonicalPath(new File(WdtcConfigPath)));
        }
        return new File(System.getProperty("user.home"));
    }

    public static String getLauncherAuthor() {
        return LAUNCHER_AUTHOR;
    }

    public static String getOs() {
        return OS;
    }
}
