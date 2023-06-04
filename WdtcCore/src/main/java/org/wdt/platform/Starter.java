package org.wdt.platform;

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

    public static String getLauncherAuthor() {
        return LAUNCHER_AUTHOR;
    }

    public static String getOs() {
        return OS;
    }
}
