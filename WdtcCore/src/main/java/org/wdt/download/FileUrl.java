package org.wdt.download;

import org.wdt.AboutSetting;

public class FileUrl {
    private static final String MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    private static final String MOJANG_ASSETS = "https://resources.download.minecraft.net/";
    private static final String MOJANG_LIBRARIES = "https://libraries.minecraft.net/";
    private static final String PISTON_META_MOJANG = "https://piston-meta.mojang.com";
    private static final String BMCALAPI_COM = "https://download.mcbbs.net/";
    private static final String PISTON_DATA_MOJANG = "https://piston-data.mojang.com";

    private static final String LITTLESKIN_URL = "https://littleskin.cn";
    private final boolean BMCLAPI;

    public FileUrl() {
        this.BMCLAPI = AboutSetting.GetBmclSwitch();
    }

    public static String getPistonDataMojang() {
        return PISTON_DATA_MOJANG;
    }

    public static String getPistonMetaMojang() {
        return PISTON_META_MOJANG;
    }

    public static String getBmcalapiCom() {
        return BMCALAPI_COM;
    }

    public static String getLittleskinUrl() {
        return LITTLESKIN_URL;
    }


    public static String getMojangVersionManifest() {
        return MOJANG_VERSION_MANIFEST;
    }

    public static String getMojangAssets() {
        return MOJANG_ASSETS;
    }

    public static String getBmclapiLibraries() {
        return BMCALAPI_COM + "maven/";
    }


    public static String getBmclapiVersionManifest() {
        return BMCALAPI_COM + "mc/game/version_manifest.json";
    }

    public static String getBmclapiAssets() {
        return BMCALAPI_COM + "assets/";
    }

    public static String getMojangLibraries() {
        return MOJANG_LIBRARIES;
    }

    public static String getLittleskinApi() {
        return LITTLESKIN_URL + "/api/yggdrasil";
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

}
