package org.wdt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOUtils;
import org.wdt.platform.PlatformUtils;

import java.io.*;
import java.util.Objects;

public class AboutSetting {
    public static File GetSettingFile() {
        return new File(System.getProperty("user.home") + "\\.wdtc\\setting\\setting.json");
    }

    public static boolean GetBmclSwitch() {
        boolean bmcl = false;
        try {
            bmcl = PlatformUtils.FileToJSONObject(GetSettingFile()).getBoolean("bmcl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmcl;
    }

    public static boolean GetLogSwitch() {
        boolean log = false;
        try {
            log = PlatformUtils.FileToJSONObject(GetSettingFile()).getBoolean("log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }

    public static boolean GetLlvmpipeSwitch() {
        boolean LlvmpipeLoader = false;
        try {
            LlvmpipeLoader = PlatformUtils.FileToJSONObject(GetSettingFile()).getBoolean("llvmpipe-loader");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LlvmpipeLoader;
    }

    public static void GenerateSettingFile() throws IOException {
        InputStream readme = AboutSetting.class.getResourceAsStream("/readme.txt");
        OutputStream writeReadme = new FileOutputStream(FilePath.getWdtcConfig() + "/readme.txt");
        IOUtils.copy(readme, writeReadme);
        if (PlatformUtils.FileExistenceAndSize(GetSettingFile())) {
            try {
                InputStream setting = AboutSetting.class.getResourceAsStream("/setting.json");
                OutputStream writeSetting = new FileOutputStream(GetSettingFile());
                IOUtils.copy(Objects.requireNonNull(setting), writeSetting);
                JSONObject Setting = PlatformUtils.FileToJSONObject(GetSettingFile());
                Setting.put("DefaultGamePath", System.getProperty("user.dir"));
                PlatformUtils.PutJSONObject(GetSettingFile(), Setting);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String GetDefaultGamePath() throws IOException {
        return PlatformUtils.FileToJSONObject(GetSettingFile()).getString("DefaultGamePath");
    }

    public static JSONObject SettingObject() throws IOException {
        return PlatformUtils.FileToJSONObject(GetSettingFile());
    }

    public static JSONArray JavaList() throws IOException {
        return SettingObject().getJSONArray("JavaPath");
    }

    public static String UserName() throws IOException {
        return UserSetting().getString("userName");
    }

    public static JSONObject UserSetting() throws IOException {
        return PlatformUtils.FileToJSONObject(FilePath.getUsersJson());
    }

    public static String GetUserType() throws IOException {
        return UserSetting().getString("type");
    }
}