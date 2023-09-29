package org.wdt.wdtc.manger;

import lombok.Getter;
import org.wdt.utils.io.FileUtils;

import java.io.File;

public class VMManger {
    public static final String LAUNCHER_VERSION = "wdtc.launcher.version";
    public static final String CONFIG_PATH = "wdtc.config.path";
    private static final String LAUNCHER_AUTHOR = "Wdt~";
    public static final String FORGE_SWITCH = "wdtc.download.forge";
    public static final String CHANGE_WINDOWS_SIZE = "wdtc.change.windows";
    public static final String DEBUG = "wdtc.debug.switch";
    public static final String CLIENT_ID = "wtdc.oauth.clientId";
    @Getter
    private static final String OS = System.getProperty("os.name");

    public static String getClientId() {
        return System.getProperty(CLIENT_ID, "8c4a5ce9-55b9-442e-9bd0-17cf89689dd0");
    }

    public static String getLauncherVersion() {
        return System.getProperty(LAUNCHER_VERSION, "demo");
    }

    public static boolean isForgeSwitch() {
        return Boolean.getBoolean(FORGE_SWITCH);
    }

    public static boolean isChangeWindowsSize() {
        return Boolean.getBoolean(CHANGE_WINDOWS_SIZE);
    }

    public static boolean isDebug() {
        return Boolean.getBoolean(DEBUG);
    }

    public static File getWdtcConfigFromVM() {
        String WdtcConfigPath = System.getProperty(CONFIG_PATH);
        return WdtcConfigPath != null
                ? new File(FileUtils.getCanonicalPath(new File(WdtcConfigPath)))
                : new File(System.getProperty("user.home"));
    }
}
