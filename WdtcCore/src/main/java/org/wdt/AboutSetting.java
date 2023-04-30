package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class AboutSetting {
    public static File GetSettingFile() {
        return new File(System.getProperty("user.home") + "\\.wdtc\\setting\\setting.json");
    }

    public static boolean GetBmclSwitch() throws IOException {
        return (boolean) StringUtil.FileToJSONObject(GetSettingFile()).get("bmcl");
    }

    public static boolean GetLogSwitch() throws IOException {
        return (boolean) StringUtil.FileToJSONObject(GetSettingFile()).get("log");
    }

    public static void GenerateSettingFile() throws IOException {
        if (!GetSettingFile().exists()) {
            FileUtils.copyFile(new File("WdtcCore/ResourceFile/setting.json"), GetSettingFile());
            JSONObject Setting = StringUtil.FileToJSONObject(GetSettingFile());
            Setting.put("DefaultGamePath", System.getProperty("user.dir"));
            FileUtils.writeStringToFile(GetSettingFile(), Setting.toString(), "UTF-8");
        }
    }

    public static String GetDefaultGamePath() throws IOException {
        return StringUtil.FileToJSONObject(GetSettingFile()).getString("DefaultGamePath");
    }
}