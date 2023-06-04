package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.wdt.platform.PlatformUtils;

import java.io.File;
import java.io.IOException;

public class FilePath {
    private static final String WDTC_CONFIG = System.getProperty("user.home") + "/.wdtc";
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
        return new File(WDTC_CONFIG + "/WdtcGameLauncherScript.bat");
    }

    public static File getAuthlibInjector() throws IOException {
        String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
        String authlib_injector_url = JSONObject.parseObject(PlatformUtils.GetUrlContent(BMCL_AUTHLIB_INJECTOR + "/artifact/latest.json")).getString("download_url");
        return new File(WDTC_CONFIG + "/" + authlib_injector_url.substring(authlib_injector_url.lastIndexOf("/") + 1));
    }

    public static File getUsersJson() {
        return new File(WDTC_CONFIG + "/users/users.json");
    }


    public static File getLlbmpipeLoader() {
        return new File(WDTC_CONFIG + "/llvmpipe-loader.jar");
    }

    public static File getWdtcCache() {
        return new File(WDTC_CONFIG + "/cache");
    }


}
