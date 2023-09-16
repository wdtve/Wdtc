package org.wdt.wdtc.manger;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.JSONUtils;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.utils.JavaUtils;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class SettingManger {
    private static final Logger logmaker = Logger.getLogger(SettingManger.class);

    public static void GenerateSettingFile() throws IOException {
        String readme = IOUtils.toString(requireNonNull(SettingManger.class.getResourceAsStream("/readme.txt")));
        File writeReadme = new File(FileManger.getWdtcConfig(), "readme.txt");
        FileUtils.writeStringToFile(writeReadme, readme);
        FileUtils.createDirectories(FileManger.getWdtcCache());
        if (PlatformUtils.FileExistenceAndSize(FileManger.getUserListFile())) {
            JSONUtils.ObjectToJsonFile(FileManger.getUserListFile(), new JsonObject());
        }
        if (PlatformUtils.FileExistenceAndSize(FileManger.getSettingFile())) {
            JSONUtils.ObjectToJsonFile(FileManger.getSettingFile(), new Setting());
        }
    }

    public static Setting getSetting() {
        try {
            return JSONUtils.JsonFileToClass(FileManger.getSettingFile(), Setting.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void putSettingToFile(Setting setting) {
        JSONUtils.ObjectToJsonFile(FileManger.getSettingFile(), setting);
    }

    @Getter
    @Setter
    @ToString
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