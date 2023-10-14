package org.wdt.wdtc.manger;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.wdt.wdtc.utils.JavaUtils;
import org.wdt.wdtc.utils.WdtcLogger;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingManger {
    private static final Logger logmaker = WdtcLogger.getLogger(SettingManger.class);


    @SneakyThrows(IOException.class)
    public static Setting getSetting() {
        return JSONUtils.JsonFileToClass(FileManger.getSettingFile(), Setting.class);
    }


    public static void putSettingToFile(Setting setting) {
        JSONUtils.ObjectToJsonFile(FileManger.getSettingFile(), setting);
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class Setting {
        private UrlManger.DownloadSourceList DownloadSource = UrlManger.DownloadSourceList.OFFICIAL;
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