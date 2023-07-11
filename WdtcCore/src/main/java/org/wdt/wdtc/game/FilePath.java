package org.wdt.wdtc.game;

import org.wdt.platform.gson.JSONObject;
import org.wdt.wdtc.platform.AboutSetting;
import org.wdt.wdtc.platform.PlatformUtils;
import org.wdt.wdtc.platform.Starter;

import java.io.File;
import java.io.IOException;

public class FilePath {
    private static final String WDTC_CONFIG = Starter.getWdtcConfigFromVM() + "/.wdtc";
    private static final File SETTING_FILE = AboutSetting.GetSettingFile();

    public static File getYggdrasilFile() {
        return new File(WDTC_CONFIG + "/users/Yggdrasil.json");
    }

    public static File getMinecraftComSkin() {
        return new File("C:/Users/yuwen/AppData/Roaming/.minecraft/assets/skins");
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

    public static File getAuthlibInjector() throws IOException {
        String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
        String authlib_injector_url = JSONObject.parseWdtObject(PlatformUtils.GetUrlContent(BMCL_AUTHLIB_INJECTOR + "/artifact/latest.json")).getString("download_url");
        return new File(getWdtcImplementationPath() + "/" + authlib_injector_url.substring(authlib_injector_url.lastIndexOf("/") + 1));
    }

    public static File getUsersJson() {
        return new File(WDTC_CONFIG + "/users/users.json");
    }


    public static File getLlbmpipeLoader() {
        return new File(getWdtcImplementationPath() + "/llvmpipe-loader.jar");
    }

    public static File getWdtcCache() {
        return new File(WDTC_CONFIG + "/cache");
    }

    public static File getJavaHomeJarFile() {
        return new File(getWdtcImplementationPath() + "/WdtcGetJavaHome.jar");
    }

    public static File getWdtcImplementationPath() {
        return new File(WDTC_CONFIG + "/implementation");
    }

}
