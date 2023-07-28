package org.wdt.wdtc.platform;


import com.google.gson.JsonArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public class AboutSetting {
    private static final Logger logmaker = Logger.getLogger(AboutSetting.class);

    public static File GetSettingFile() {
        return new File(FilePath.getWdtcConfig() + "/setting/setting.json");
    }

    public static void GenerateSettingFile() throws IOException {
        String readme = IOUtils.toString(requireNonNull(AboutSetting.class.getResourceAsStream("/readme.txt")), StandardCharsets.UTF_8);
        File writeReadme = new File(FilePath.getWdtcConfig() + "/readme.txt");
        FileUtils.writeStringToFile(writeReadme, readme);
        if (PlatformUtils.FileExistenceAndSize(GetSettingFile())) {
            FileUtils.writeStringToFile(GetSettingFile(), JSONObject.toJSONString(new Setting()), "UTF-8");
        }
    }

    public static JSONObject SettingObject() {
        try {
            return JSONUtils.getJSONObject(GetSettingFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Setting getSetting() {
        return PlatformUtils.JsonFileToClass(GetSettingFile(), Setting.class);
    }

    public static String getPreferredVersion() {
        return SettingObject().getString("PreferredVersion");
    }

    public static void putSettingToFile(Setting setting) {
        try {
            FileUtils.writeStringToFile(GetSettingFile(), JSONObject.toJSONString(setting), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Setting {
        public boolean bmcl = false;
        public boolean console = false;
        public boolean LlvmpipeLoader = false;
        public String DefaultGamePath = System.getProperty("user.dir");
        public JsonArray JavaPath = new JsonArray();
        public boolean ChineseLanguage = true;
        public double WindowsWidth = 616.0;
        public double WindowsHeight = 489.0;
        public String PreferredVersion;

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

        public boolean isBmcl() {
            return bmcl;
        }

        public void setBmcl(boolean bmcl) {
            this.bmcl = bmcl;
        }

        public boolean isConsole() {
            return console;
        }

        public void setConsole(boolean console) {
            this.console = console;
        }

        public boolean isLlvmpipeLoader() {
            return LlvmpipeLoader;
        }

        public void setLlvmpipeLoader(boolean llvmpipeLoader) {
            LlvmpipeLoader = llvmpipeLoader;
        }

        public String getDefaultGamePath() {
            return DefaultGamePath;
        }

        public void setDefaultGamePath(String defaultGamePath) {
            DefaultGamePath = defaultGamePath;
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

        @Override
        public String toString() {
            return "Setting{" +
                    "bmcl=" + bmcl +
                    ", console=" + console +
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