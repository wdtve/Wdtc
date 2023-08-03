package org.wdt.wdtc.download;

import org.wdt.wdtc.download.downloadsource.BmclDownloadSource;
import org.wdt.wdtc.download.downloadsource.McbbsDownloadSource;
import org.wdt.wdtc.download.downloadsource.OfficialDownloadSource;
import org.wdt.wdtc.download.infterface.DownloadSource;
import org.wdt.wdtc.platform.AboutSetting;

public class FileUrl {
    public static final String MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    public static final String MOJANG_ASSETS = "https://resources.download.minecraft.net/";
    public static final String MOJANG_LIBRARIES = "https://libraries.minecraft.net/";
    public static final String PISTON_META_MOJANG = "https://piston-meta.mojang.com";
    public static final String BMCALAPI_COM = "https://download.mcbbs.net/";
    public static final String PISTON_DATA_MOJANG = "https://piston-data.mojang.com";

    public static final String LITTLESKIN_URL = "https://littleskin.cn";
    public static final String ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/";


    public static String getPistonDataMojang() {
        return PISTON_DATA_MOJANG;
    }

    public static String getPistonMetaMojang() {
        return PISTON_META_MOJANG;
    }


    public static String getLittleskinUrl() {
        return LITTLESKIN_URL;
    }



    public static String getMojangLibraries() {
        return MOJANG_LIBRARIES;
    }

    public static String getLittleskinApi() {
        return LITTLESKIN_URL + "/api/yggdrasil";
    }


    public enum DownloadSourceList {
        OFFICIAL,
        BMCLAPI,
        MCBBS;

        public static DownloadSource getDownloadSource() {
            DownloadSourceList source = AboutSetting.getSetting().getDownloadSource();
            if (source == OFFICIAL) {
                return new OfficialDownloadSource();
            } else if (source == BMCLAPI) {
                return new BmclDownloadSource();
            } else {
                return new McbbsDownloadSource();
            }
        }

        public static boolean NoOfficialDownloadSource() {
            return AboutSetting.getSetting().getDownloadSource() != OFFICIAL;
        }
    }
}
