package org.wdt;

import java.io.File;

public class FilePath {
    private static final String VERSION_MANIFEST_JSON = "WdtcCore/ResourceFile/Download/version_manifest.json";
    private static final String LAUNCHER_JSON = "WdtcCore/ResourceFile/Launcher/launcher.json";
    private static final String STARTER_BAT = "WdtcCore/ResourceFile/Launcher/starter.bat";
    private static final String USERS_JSON = "WdtcCore/ResourceFile/Launcher/users/users.json";
    private static final String USERS_SETTING_JSON = "WdtcCore/ResourceFile/Launcher/users/UsersSetting.json";
    private static final String log4j2 = "WdtcCore/ResourceFile/Download/log4j2.xml";


    public static File getLauncherJson() {
        return new File(LAUNCHER_JSON);
    }

    public static File getLog4j2() {
        return new File(log4j2);
    }

    public static File getStarterBat() {
        return new File(STARTER_BAT);
    }

    public static File getUsersJson() {
        return new File(USERS_JSON);
    }

    public static File getUsersSettingJson() {
        return new File(USERS_SETTING_JSON);
    }

    public static File getVersionManifestJson() {
        return new File(VERSION_MANIFEST_JSON);
    }
}
