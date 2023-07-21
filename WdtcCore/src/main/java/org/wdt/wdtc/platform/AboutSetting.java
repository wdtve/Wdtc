package org.wdt.wdtc.platform;


import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.platform.gson.JSONArray;
import org.wdt.platform.gson.JSONObject;
import org.wdt.platform.gson.JSONUtils;
import org.wdt.wdtc.game.FilePath;
import org.wdt.wdtc.utils.PlatformUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class AboutSetting {
    private static final Logger logmaker = Logger.getLogger(AboutSetting.class);

    public static File GetSettingFile() {
        return new File(FilePath.getWdtcConfig() + "/setting/setting.json");
    }

    public static boolean GetBmclSwitch() {
        return SettingObject().getBoolean("bmcl");
    }

    public static boolean GetLogSwitch() {
        return SettingObject().getBoolean("log");
    }

    public static boolean GetLlvmpipeSwitch() {
        return SettingObject().getBoolean("llvmpipe-loader");
    }

    public static boolean GetZHCNSwitch() {
        return SettingObject().getBoolean("ZH-CN");
    }

    public static void GenerateSettingFile() throws IOException {
        String readme = IOUtils.toString(requireNonNull(AboutSetting.class.getResourceAsStream("/readme.txt")), StandardCharsets.UTF_8);
        File writeReadme = new File(FilePath.getWdtcConfig() + "/readme.txt");
        FileUtils.writeStringToFile(writeReadme, readme);
        if (PlatformUtils.FileExistenceAndSize(GetSettingFile())) {
            Map<String, Object> ObjectMap = new HashMap<>();
            ObjectMap.put("bmcl", false);
            ObjectMap.put("log", false);
            ObjectMap.put("llvmpipe-loader", false);
            ObjectMap.put("DefaultGamePath", System.getProperty("user.dir"));
            ObjectMap.put("JavaPath", new ArrayList<>());
            ObjectMap.put("ZH-CN", true);
            Gson gson = new Gson();
            FileUtils.writeStringToFile(GetSettingFile(), gson.toJson(ObjectMap), "UTF-8");
        }
    }

    public static String GetDefaultGamePath() {
        return SettingObject().getString("DefaultGamePath");
    }

    public static double GetWindowsWidth() {
        return SettingObject().getDouble("WindowsWidth");
    }

    public static double GetWindowsHeight() {
        return SettingObject().getDouble("WindowsHeight");
    }

    public static JSONObject SettingObject() {
        try {
            return JSONUtils.getJSONObject(GetSettingFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray JavaList() {
        return SettingObject().getJSONArray("JavaPath");
    }
}