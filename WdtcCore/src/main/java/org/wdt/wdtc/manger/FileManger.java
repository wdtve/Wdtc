package org.wdt.wdtc.manger;

import java.io.File;

public class FileManger {



    public static File getMinecraftComSkin() {
        return new File(System.getProperty("user.home"), "AppData/Roaming/.minecraft/assets/skins");
    }



    public static File getWdtcConfig() {
        return new File(VMManger.getWdtcConfigFromVM(), ".wdtc");
    }


    public static File getStarterBat() {
        return new File(getWdtcCache(), "WdtcGameLauncherScript.bat");
    }

    public static File getAuthlibInjector() {
        return new File(getWdtcImplementationPath(), "authlib-injector.jar");
    }

    public static File getUsersJson() {
        return new File(getWdtcUser(), "user.json");
    }

    public static File getLlbmpipeLoader() {
        return new File(getWdtcImplementationPath(), "llvmpipe-loader.jar");
    }

    public static File getWdtcCache() {
        return new File(getWdtcConfig(), "cache");
    }

    public static File getWdtcImplementationPath() {
        return new File(getWdtcConfig(), "dependencies");
    }

    public static File getWtdcOpenJFXPath() {
        return new File(getWdtcImplementationPath(), "openjfx");
    }

    public static File getWdtcUser() {
        return new File(getWdtcConfig(), "users");
    }

    public static File getUserAsste() {
        return new File(getWdtcUser(), "assets");
    }

    public static File getUserListFile() {
        return new File(getWdtcUser(), "users.json");
    }

    public static File getSettingFile() {
        return new File(getWdtcConfig(), "setting/setting.json");
    }

    public static File getVersionManifestFile() {
        return new File(getWdtcCache(), "versionManifest.json");
    }
}
