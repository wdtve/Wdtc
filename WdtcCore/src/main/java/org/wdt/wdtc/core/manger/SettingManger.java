package org.wdt.wdtc.core.manger;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.wdtc.core.utils.JavaUtils;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SettingManger {
  private static final Logger logmaker = WdtcLogger.getLogger(SettingManger.class);


  @SneakyThrows(IOException.class)
  public static Setting getSetting() {
    return JsonUtils.readFileToClass(FileManger.getSettingFile(), Setting.class);
  }


  @SneakyThrows
  public static void putSettingToFile(Setting setting) {
    JsonUtils.writeObjectToFile(FileManger.getSettingFile(), setting, Json.getBuilder().setPrettyPrinting());
  }

  @Data
  @Accessors(chain = true)
  public static class Setting {
    private final boolean isConsole = VMManger.isConsole();
    private DownloadSourceManger.DownloadSourceList DownloadSource = DownloadSourceManger.DownloadSourceList.OFFICIAL;
    private boolean Console = false;
    private boolean LlvmpipeLoader = false;
    private File DefaultGamePath = new File(System.getProperty("user.dir"));
    private Set<JavaUtils.JavaInfo> JavaPath = new HashSet<>();
    private boolean ChineseLanguage = true;
    private double WindowsWidth = 616.0;
    private double WindowsHeight = 489.0;
    private String PreferredVersion;
  }
}