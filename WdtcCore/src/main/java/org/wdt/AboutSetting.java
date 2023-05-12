package org.wdt;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class AboutSetting {
    public static File GetSettingFile() {
        return new File(System.getProperty("user.home") + "\\.wdtc\\setting\\setting.json");
    }

    public static boolean GetBmclSwitch() {
        boolean bmcl = false;
        try {
            bmcl = (boolean) StringUtil.FileToJSONObject(GetSettingFile()).get("bmcl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmcl;
    }

    public static boolean GetLogSwitch() {
        boolean log = false;
        try {
            log = (boolean) StringUtil.FileToJSONObject(GetSettingFile()).get("log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }

    public static boolean GetLlvmpipeSwitch() {
        boolean LlvmpipeLoader = false;
        try {
            LlvmpipeLoader = (boolean) StringUtil.FileToJSONObject(GetSettingFile()).get("llvmpipe-loader");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LlvmpipeLoader;
    }

    public static void GenerateSettingFile() {
        if (StringUtil.FileExistenceAndSize(GetSettingFile())) {
            try {
                FileUtils.copyFile(new File("ResourceFile/setting.json"), GetSettingFile());
                JSONObject Setting = StringUtil.FileToJSONObject(GetSettingFile());
                Setting.put("DefaultGamePath", System.getProperty("user.dir"));
                StringUtil.PutJSONObject(GetSettingFile(), Setting);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String GetDefaultGamePath() throws IOException {
        return StringUtil.FileToJSONObject(GetSettingFile()).getString("DefaultGamePath");
    }
}