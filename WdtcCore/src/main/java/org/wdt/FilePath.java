package org.wdt;

import java.io.File;

public class FilePath {
    private static final String LAUNCHER_JSON = "ResourceFile/Launcher/launcher.json";
    private static final String STARTER_BAT = "ResourceFile/Launcher/WdtcGameLauncherScript.bat";
    private static final String USERS_JSON = System.getProperty("user.home") + "/.wdtc/users/users.json";
    private static final String log4j2 = "ResourceFile/log4j2.xml";
    private static final String LLBMPIPE_LOADER = System.getProperty("user.home") + "/.wdtc/llvmpipe-loader.jar";
    private static final String AUTHLIB_INJECTOR = System.getProperty("user.home") + "/.wdtc/authlib-injector.jar";
    private static final String WDTC_CONFIG = System.getProperty("user.home") + "/.wdtc";
    private static final File SETTING_FILE = AboutSetting.GetSettingFile();
    private static final String MINECRAFT_COM_SKIN = "C:/Users/yuwen/AppData/Roaming/.minecraft/assets/skins";
    private static final String YGGDRASIL_FILE = System.getProperty("user.home") + "/.wdtc/users/Yggdrasil.json";

    public static File getYggdrasilFile() {
        return new File(YGGDRASIL_FILE);
    }

    public static File getMinecraftComSkin() {
        return new File(MINECRAFT_COM_SKIN);
    }

    public static File getSettingFile() {
        return SETTING_FILE;
    }

    public static File getWdtcConfig() {
        return new File(WDTC_CONFIG);
    }

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


    public static File getLlbmpipeLoader() {
        return new File(LLBMPIPE_LOADER);
    }


}