package org.wdt.WdtcLauncher;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.log4j.Logger;
import org.wdt.AboutSetting;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetJavaPath {
    private static final Logger logmaker = Logger.getLogger(GetJavaPath.class);

    public static String GetRunJavaHome() {
        return "\"" + System.getProperty("java.home") + "\\bin\\java.exe\"";
    }

    public static void GetAllJavaHome() {
        File[] dishes = File.listRoots();
        for (File file : dishes) {
            FindJava(file);
        }
    }

    public static void FindJava(File dir) {
        try {
            JSONObject SettingObject = AboutSetting.SettingObject();
            JSONArray JavaList = AboutSetting.JavaList();
            File[] files = dir.listFiles();
            if (Objects.nonNull(files)) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        FindJava(file.getAbsoluteFile());
                    } else if (file.isFile() && "java.exe".equals(file.getName())) {
                        try {
                            File ReleaseFile = new File(file.getParent());
                            List<String> allLines = Files.readAllLines(Paths.get(ReleaseFile.getParent() + "/release"));
                            for (String line : allLines) {
                                Pattern p = Pattern.compile("JAVA_VERSION=");
                                Matcher m = p.matcher(line);
                                if (m.find()) {
                                    logmaker.info("* 已找到Java: " + file.getAbsolutePath() + " 版本: " + line.replace("JAVA_VERSION=", "").replace("\"", ""));
                                    AddPath(SettingObject, JavaList, file);
                                }
                            }
                        } catch (IOException e) {
                            logmaker.info("* 已找到Java:" + file.getAbsolutePath());
                            AddPath(SettingObject, JavaList, file);
                            break;
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            logmaker.info("错误: ", e);
        }
    }

    private static void AddPath(JSONObject SettingObject, JSONArray JavaList, File file) {
        boolean AddPath = true;
        for (int i = 0; i < JavaList.size(); i++) {
            if (AddPath) {
                if (file.getAbsolutePath().equals(JavaList.getString(i))) {
                    AddPath = false;
                }
            }
        }
        if (AddPath) {
            SettingObject.getJSONArray("JavaPath").add(file.getAbsoluteFile());
            StringUtil.PutJSONObject(AboutSetting.GetSettingFile(), SettingObject);
        }

    }
}

