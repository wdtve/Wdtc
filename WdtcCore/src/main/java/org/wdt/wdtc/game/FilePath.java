package org.wdt.wdtc.game;

import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.Starter;

import java.io.File;

public class FilePath {
    private static final String WDTC_CONFIG = Starter.getWdtcConfigFromVM() + "/.wdtc/";
    private static final File SETTING_FILE = AboutSetting.GetSettingFile();


    public static File getMinecraftComSkin() {
        return new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/assets/skins/");
    }

    public static File getSettingFile() {
        return SETTING_FILE;
    }

    public static File getWdtcConfig() {
        return new File(WDTC_CONFIG);
    }


    public static File getStarterBat() {
        return new File(getWdtcCache() + "/WdtcGameLauncherScript.bat");
    }

    public static File getAuthlibInjector() {
        return new File(getWdtcImplementationPath() + "/authlib-injector.jar");
    }

    public static File getUsersJson() {
        return new File(WDTC_CONFIG + "/users/users.json");
    }


    public static File getLlbmpipeLoader() {
        return new File(getWdtcImplementationPath() + "/llvmpipe-loader.jar");
    }

    public static File getWdtcCache() {
        return new File(WDTC_CONFIG + "/cache/");
    }

    public static File getWdtcImplementationPath() {
        return new File(WDTC_CONFIG + "/dependencies/");
    }

    public static File getWtdcOpenJFXPath() {
        return new File(getWdtcImplementationPath() + "/openjfx/");
    }

}
