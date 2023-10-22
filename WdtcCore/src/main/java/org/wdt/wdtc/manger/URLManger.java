package org.wdt.wdtc.manger;

import org.wdt.wdtc.download.downloadsource.BmclDownloadSource;
import org.wdt.wdtc.download.downloadsource.McbbsDownloadSource;
import org.wdt.wdtc.download.downloadsource.OfficialDownloadSource;
import org.wdt.wdtc.download.infterface.DownloadSource;

public class URLManger {
    public static final String BMCALAPI_COM = "https://download.mcbbs.net/";

    public static final String LITTLESKIN_URL = "https://littleskin.cn";
    public static final String ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/";


    public static String getPistonDataMojang() {
        return DownloadSource.PISTON_DATA_MOJANG;
    }

    public static String getPistonMetaMojang() {
        return DownloadSource.PISTON_META_MOJANG;
    }


    public static String getLittleskinUrl() {
        return LITTLESKIN_URL;
    }



    public static String getMojangLibraries() {
        return DownloadSource.MOJANG_LIBRARIES;
    }

    public static String getLittleskinApi() {
        return LITTLESKIN_URL + "/api/yggdrasil";
    }


    public enum DownloadSourceList {
        OFFICIAL,
        BMCLAPI,
        MCBBS;

        public static DownloadSource getDownloadSource() {
            DownloadSourceList source = SettingManger.getSetting().getDownloadSource();
            if (source == OFFICIAL) {
                return new OfficialDownloadSource();
            } else if (source == BMCLAPI) {
                return new BmclDownloadSource();
            } else {
                return new McbbsDownloadSource();
            }
        }

        public static boolean NoOfficialDownloadSource() {
            return SettingManger.getSetting().getDownloadSource() != OFFICIAL;
        }
    }
}
