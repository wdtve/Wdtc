package org.WdtcDownload;

public class FileUrl {
    private static final String MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    private static final String MOJANG_ASSETS = "https://resources.download.minecraft.net/";
    private static final String BMCLAPI_LIBRARIES = "https://download.mcbbs.net/maven/";
    private static final String FABRAIC_LIST = "https://meta.fabricmc.net/v2/versions/loader";
    private static final String BMCLAPI_VERSION_MANIFEST = "https://download.mcbbs.net/mc/game/version_manifest.json";
    private static final String BMCLAPI_ASSETS = "https://download.mcbbs.net/assets/";
    private static final String MOJANG_LIBRARIES = "https://libraries.minecraft.net/";

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

}
