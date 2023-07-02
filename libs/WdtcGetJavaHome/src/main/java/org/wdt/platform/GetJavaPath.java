package org.wdt.platform;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetJavaPath {
    private static final File WdtcSettingFile = new File(System.getProperty("user.home") + "\\.wdtc\\setting\\setting.json");

    public static String GetRunJavaHome() {
        return "\"" + System.getProperty("java.home") + "\\bin\\java.exe\"";
    }

    public static void main(String[] args) throws IOException {
        File[] roots = File.listRoots();
        for (File file : roots) {
            FindJava(file);
        }
    }

    public static void FindJava(File dir) {
        try {
            JSONObject SettingObject = JSONObject.parseObject(FileUtils.readFileToString(WdtcSettingFile, "UTF-8"));
            JSONArray JavaList = SettingObject.getJSONArray("JavaPath");
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
                                    System.out.println("* Find Java: \"" + file.getAbsolutePath() + "\" Version: " + line.replace("JAVA_VERSION=", "").replace("\"", ""));
                                    AddPath(SettingObject, JavaList, file);
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("* Find Java: \"" + file.getAbsolutePath() + "\"");
                            AddPath(SettingObject, JavaList, file);
                            break;
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void AddPath(JSONObject SettingObject, JSONArray JavaList, File file) throws IOException {
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
            FileUtils.writeStringToFile(WdtcSettingFile, SettingObject.toString(), "UTF-8");
        }

    }
}
