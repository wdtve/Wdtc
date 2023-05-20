package org.wdt.WdtcDownload;

import org.wdt.AboutSetting;

public class FileUrl {
    private static final String MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    private static final String MOJANG_ASSETS = "https://resources.download.minecraft.net/";
    private static final String BMCLAPI_LIBRARIES = "https://download.mcbbs.net/maven/";
    private static final String FABRAIC_LIST = "https://meta.fabricmc.net/v2/versions/loader";
    private static final String BMCLAPI_VERSION_MANIFEST = "https://download.mcbbs.net/mc/game/version_manifest.json";
    private static final String BMCLAPI_ASSETS = "https://download.mcbbs.net/assets/";
    private static final String MOJANG_LIBRARIES = "https://libraries.minecraft.net/";
    private static final String FABRIC_FILE_LIST = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";
    private static final String BMCLAPI_FABRIC_FILE_LIST = "https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader/%s/%s";
    private static final String BMCL_FABRIC_FILE_LIST = "https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader/%s/%s";
    private static final String LLBMPIPE_LOADER = "https://ghdl.feizhuqwq.cf/https://github.com/Glavo/llvmpipe-loader/releases/download/v1.0/llvmpipe-loader-1.0.jar";
    private static final String AUTHLIB_INJECTOR = "https://authlib-injector.yushi.moe/";
    private static final String BMCL_AUTHLIB_INJECTOR = "https://bmclapi2.bangbang93.com/mirrors/authlib-injector/";
    private static final String LITTLESKIN_API = "https://littleskin.cn/api/yggdrasil";
    private static final String FABRIC_MAVEN = "https://maven.fabricmc.net/";
    private static boolean BMCLAPI;

    public FileUrl() {
        FileUrl.BMCLAPI = AboutSetting.GetBmclSwitch();
    }

    public static String getFabricMaven() {
        return FABRIC_MAVEN;
    }

    public static String getLittleskinApi() {
        return LITTLESKIN_API;
    }

    public static String getBmclapiFabricFileList() {
        return BMCLAPI_FABRIC_FILE_LIST;
    }

    public static String getAuthlibInjector() {
        return AUTHLIB_INJECTOR;
    }

    public static String getBmclAuthlibInjector() {
        return BMCL_AUTHLIB_INJECTOR;
    }

    public static String getLlbmpipeLoader() {
        return LLBMPIPE_LOADER;
    }

    public static String getBmclFabricFileList() {
        return BMCL_FABRIC_FILE_LIST;
    }

    public static String getFabricFileList() {
        return FABRIC_FILE_LIST;
    }

    public static String getMojangVersionManifest() {
        return MOJANG_VERSION_MANIFEST;
    }

    public static String getMojangAssets() {
        return MOJANG_ASSETS;
    }

    public static String getBmclapiLibraries() {
        return BMCLAPI_LIBRARIES;
    }

    public static String getFabraicList() {
        return FABRAIC_LIST;
    }

    public static String getBmclapiVersionManifest() {
        return BMCLAPI_VERSION_MANIFEST;
    }

    public static String getBmclapiAssets() {
        return BMCLAPI_ASSETS;
    }

    public static String getMojangLibraries() {
        return MOJANG_LIBRARIES;
    }

    public String getVersionManifest() {
        if (BMCLAPI) {
            return getBmclapiVersionManifest();
        } else {
            return getMojangVersionManifest();
        }
    }

    public String getAssets() {
        if (BMCLAPI) {
            return getBmclapiAssets();
        } else {
            return getMojangAssets();
        }
    }

    public String getLibrary() {
        if (BMCLAPI) {
            return getBmclapiLibraries();
        } else {
            return getMojangLibraries();
        }
    }

    public String GetAuthlibInjector() {
        if (BMCLAPI) {
            return getBmclAuthlibInjector();
        } else {
            return getAuthlibInjector();
        }
    }

}
