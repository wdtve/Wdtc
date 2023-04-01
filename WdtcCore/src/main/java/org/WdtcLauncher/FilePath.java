package org.WdtcLauncher;

public class FilePath {
    private static final String VERSION_MANIFEST_JSON = "WdtcCore/ResourceFile/Download/version_manifest.json";
    private static final String LAUNCHER_JSON = "WdtcCore/ResourceFile/Launcher/launcher.json";
    private static final String STARTER_BAT = "WdtcCore/ResourceFile/Launcher/starter.bat";
    private static final String USERS_JSON = "WdtcCore/ResourceFile/Launcher/users/users.json";
    private static final String USERS_SETTING_JSON = "WdtcCore/ResourceFile/Launcher/users/UsersSetting.json";
    private static final String log4j2 = "WdtcCore/ResourceFile/Download/log4j2.xml";
    private static final String resources_zip = "WdtcCore/ResourceFile/Download/objects.jar";

    public static String getResources_zip() {
        return resources_zip;
    }

    public static String getLauncherJson() {
        return LAUNCHER_JSON;
    }

    public static String getLog4j2() {
        return log4j2;
    }

    public static String getStarterBat() {
        return STARTER_BAT;
    }

    public static String getUsersJson() {
        return USERS_JSON;
    }

    public static String getUsersSettingJson() {
        return USERS_SETTING_JSON;
    }

    public static String getVersionManifestJson() {
        return VERSION_MANIFEST_JSON;
    }
}
