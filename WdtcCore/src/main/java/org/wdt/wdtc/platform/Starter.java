package org.wdt.wdtc.platform;

import java.util.Objects;

public class Starter {
    private static final String LAUNCHER_VERSION = "Demo";
    private static final String LAUNCHER_AUTHOR = "Wdt~";
    private static final String OS = System.getProperty("os.name");

    public static String getLauncherVersion() {
        if (Objects.nonNull(System.getProperty("launcher.version"))) {
            return System.getProperty("launcher.version");
        } else {
            return LAUNCHER_VERSION;
        }
    }

    public static boolean getForgeSwitch() {
        if (Objects.nonNull(System.getProperty("download.forge.true"))) {
            return true;
        } else if (Objects.nonNull(System.getProperty("download.forge.false"))) {
            return false;
        } else {
            return false;
        }
    }

    public static String getWdtcConfigFromVM() {
        String WdtcConfig = System.getProperty("wdtc.config.path");
        if (Objects.nonNull(WdtcConfig)) {
            return WdtcConfig;
        } else {
            return System.getProperty("user.home");
        }
    }

    public static String getLauncherAuthor() {
        return LAUNCHER_AUTHOR;
    }

    public static String getOs() {
        return OS;
    }
}
