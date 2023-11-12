package org.wdt.wdtc.core.manger;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.wdt.wdtc.core.utils.JavaUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;
import org.wdt.wdtc.core.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingManger {
  private static final Logger logmaker = WdtcLogger.getLogger(SettingManger.class);


  @SneakyThrows(IOException.class)
  public static Setting getSetting() {
    return JSONUtils.readJsonFileToClass(FileManger.getSettingFile(), Setting.class);
  }


  public static void putSettingToFile(Setting setting) {
    JSONUtils.writeObjectToJsonFile(FileManger.getSettingFile(), setting);
  }

  @Data
  @Accessors(chain = true)
  public static class Setting {
    private DownloadSourceManger.DownloadSourceList DownloadSource = DownloadSourceManger.DownloadSourceList.OFFICIAL;
    private boolean Console = false;
    private boolean LlvmpipeLoader = false;
    private File DefaultGamePath = new File(System.getProperty("user.dir"));
    private List<JavaUtils.JavaInfo> JavaPath = new ArrayList<>();
    private boolean ChineseLanguage = true;
    private double WindowsWidth = 616.0;
    private double WindowsHeight = 489.0;
    private String PreferredVersion;
  }
}