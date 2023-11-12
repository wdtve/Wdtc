package org.wdt.wdtc.core.manger;

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface;

public class URLManger {
  public static final String BMCALAPI_COM = "https://download.mcbbs.net/";
  public static final String ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/";


  public static String getPistonDataMojang() {
    return DownloadSourceInterface.PISTON_DATA_MOJANG;
  }

  public static String getPistonMetaMojang() {
    return DownloadSourceInterface.PISTON_META_MOJANG;
  }

  public static String getLittleskinUrl() {
    return "https://littleskin.cn";
  }


  public static String getMojangLibraries() {
    return DownloadSourceInterface.MOJANG_LIBRARIES;
  }

  public static String getLittleskinApi() {
    return getLittleskinUrl() + "/api/yggdrasil";
  }


}
