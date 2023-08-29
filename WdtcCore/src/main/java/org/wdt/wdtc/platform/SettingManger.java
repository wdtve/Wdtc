package org.wdt.wdtc.platform;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.utils.FileUtils;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.download.UrlManger;
import org.wdt.wdtc.game.FileManger;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;

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

    public static class Setting {
        private UrlManger.DownloadSourceList DownloadSource = UrlManger.DownloadSourceList.OFFICIAL;
        private boolean Console = false;
        private boolean LlvmpipeLoader = false;
        private String DefaultGamePath = System.getProperty("user.dir");
        private JsonArray JavaPath = new JsonArray();
        private boolean ChineseLanguage = true;
        private double WindowsWidth = 616.0;
        private double WindowsHeight = 489.0;
        private String PreferredVersion;

        public String getPreferredVersion() {
            return PreferredVersion;
        }

        public void setPreferredVersion(String preferredVersion) {
            PreferredVersion = preferredVersion;
        }

        public double getWindowsWidth() {
            return WindowsWidth;
        }

        public void setWindowsWidth(double windowsWidth) {
            WindowsWidth = windowsWidth;
        }

        public double getWindowsHeight() {
            return WindowsHeight;
        }

        public void setWindowsHeight(double windowsHeight) {
            WindowsHeight = windowsHeight;
        }

        public boolean isConsole() {
            return Console;
        }

        public void setConsole(boolean console) {
            this.Console = console;
        }

        public boolean isLlvmpipeLoader() {
            return LlvmpipeLoader;
        }

        public void setLlvmpipeLoader(boolean llvmpipeLoader) {
            LlvmpipeLoader = llvmpipeLoader;
        }

        public File getDefaultGamePath() {
            return new File(DefaultGamePath);
        }

        public void setDefaultGamePath(File defaultGamePath) {
            DefaultGamePath = FileUtils.getCanonicalPath(defaultGamePath);
        }

        public JsonArray getJavaPath() {
            return JavaPath;
        }

        public void setJavaPath(JsonArray javaPath) {
            JavaPath = javaPath;
        }

        public boolean isChineseLanguage() {
            return ChineseLanguage;
        }

        public void setChineseLanguage(boolean chineseLanguage) {
            ChineseLanguage = chineseLanguage;
        }

        public UrlManger.DownloadSourceList getDownloadSource() {
            return DownloadSource;
        }

        public void setDownloadSource(UrlManger.DownloadSourceList downloadSource) {
            DownloadSource = downloadSource;
        }

        @Override
        public String toString() {
            return "Setting{" +
                    "DownloadSource=" + DownloadSource +
                    ", Console=" + Console +
                    ", LlvmpipeLoader=" + LlvmpipeLoader +
                    ", DefaultGamePath='" + DefaultGamePath + '\'' +
                    ", JavaPath=" + JavaPath +
                    ", ChineseLanguage=" + ChineseLanguage +
                    ", WindowsWidth=" + WindowsWidth +
                    ", WindowsHeight=" + WindowsHeight +
                    ", PreferredVersion='" + PreferredVersion + '\'' +
                    '}';
        }
    }
}