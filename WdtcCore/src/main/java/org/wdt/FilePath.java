package org.wdt;

import java.io.File;

public class FilePath {
    private static final String LAUNCHER_JSON = "ResourceFile/Launcher/launcher.json";
    private static final String STARTER_BAT = "ResourceFile/Launcher/WdtcGameLauncherScript.bat";
    private static final String USERS_JSON = System.getProperty("user.home") + "/.wdtc/users/users.json";
    private static final String USERS_SETTING_JSON = System.getProperty("user.home") + "/.wdtc/users/UsersSetting.json";
    private static final String log4j2 = "ResourceFile/log4j2.xml";
    private static final String LLBMPIPE_LOADER = System.getProperty("user.home") + "/.wdtc/llvmpipe-loader.jar";
    private static final String AUTHLIB_INJECTOR = System.getProperty("user.home") + "/.wdtc/authlib-injector.jar";

    public static File getLauncherJson() {
        return new File(LAUNCHER_JSON);
    }

    public static File getLog4j2() {
        return new File(log4j2);
    }

    public static File getStarterBat() {
        return new File(STARTER_BAT);
    }

    public static File getAuthlibInjector() {
        return new File(AUTHLIB_INJECTOR);
    }

    public static File getUsersJson() {
        return new File(USERS_JSON);
    }

    public static File getUsersSettingJson() {
        return new File(USERS_SETTING_JSON);
    }

    public static File getLlbmpipeLoader() {
        return new File(LLBMPIPE_LOADER);
    }


}
