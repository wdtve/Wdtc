package org.wdt.wdtc.platform;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Starter {
    private static final String LAUNCHER_VERSION = System.getProperty("launcher.version", "Demo");
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
        return System.getProperty("download.forge.true") != null;
    }

    public static String getWdtcConfigFromVM() {
        String WdtcConfig = System.getProperty("wdtc.config.path");
        if (Objects.nonNull(WdtcConfig)) {
            try {
                return new File(WdtcConfig).getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
