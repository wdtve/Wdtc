package org.wdt.wdtc.utils;

import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.wdt.utils.IOUtils;
import org.wdt.wdtc.platform.SettingManger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(JavaUtils.class);

    public static String GetRunJavaHome() {
        return getJavaExePath(System.getProperty("java.home"));
    }

    public static void main(String[] args) {
        try {
            getPotentialJava("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit");
            getPotentialJava("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Update");
            getPotentialJava("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Web Start Caps");
            getPotentialJava("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK");
        } catch (IOException e) {
            logmaker.error("Error, ", e);
        }
    }

    private static void getPotentialJava(String key) throws IOException {
        Process process = new ProcessBuilder("reg", "query", key).start();
        SettingManger.Setting setting = SettingManger.getSetting();
        JsonArray JavaList = setting.getJavaPath();
        for (String s : IOUtils.readLines(process.getInputStream())) {
            if (s.startsWith(key)) {
                for (Map<String, String> map : getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
                    String JavaPath = map.get("JavaPath");
                    boolean AddPath = true;
                    for (int i = 0; i < JavaList.size(); i++) {
                        if (AddPath) {
                            if (JavaPath.equals(JavaList.get(i).getAsString())) {
                                AddPath = false;
                            }
                        }
                    }
                    if (AddPath) {
                        JavaList.add(JavaPath);
                        logmaker.info("Find Java : " + map.get("JavaPath") + ", Version : " + map.get("JavaVersion"));
                    }
                }
            }
        }
        SettingManger.putSettingToFile(setting);
    }


    private static List<String> getPotentialJavaFolders(String key) throws IOException {
        List<String> List = new ArrayList<>();
        Process process = new ProcessBuilder("reg", "query", key).start();
        for (String s : IOUtils.readLines(process.getInputStream())) {
            if (s.startsWith(key)) {
                List.add(s);
            }
        }
        return List;
    }

    private static List<String> getPotentialJavaHome(List<String> list) throws IOException {
        List<String> JavaHomeList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)");
        for (String key : list) {
            Process process = new ProcessBuilder("reg", "query", key, "/v", "JavaHome").start();
            for (String s : IOUtils.readLines(process.getInputStream())) {
                String JavaHomeCleaned = s.replaceAll("\\s", "");
                if (JavaHomeCleaned.startsWith("JavaHome")) {
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        JavaHomeList.add(matcher.group(1));
                    }
                }
            }
        }
        return JavaHomeList;
    }

    public static List<Map<String, String>> getJavaExeAndVersion(List<String> list) throws IOException {
        List<Map<String, String>> JavaList = new ArrayList<>();
        for (String path : list) {
            String JavaPath = getJavaExePath(path);
            if (!Files.exists(Paths.get(path))) {
                logmaker.warn("warn : ", new IOException(path + " isn't exists"));
            } else {
                if (!PlatformUtils.FileExistenceAndSize(JavaPath)) {
                    Map<String, String> JavaExeAndVersion = new HashMap<>();
                    JavaExeAndVersion.put("JavaPath", JavaPath);
                    JavaExeAndVersion.put("JavaVersion", getJavaVersion(JavaPath));
                    JavaList.add(JavaExeAndVersion);
                }
            }
        }
        return JavaList;
    }

    public static String getJavaVersion(String JavaPath) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{JavaPath, "-XshowSettings:properties", "-version"});
            Pattern pattern = Pattern.compile("java\\.version = (.+)");
            for (String line : IOUtils.readLines(process.getErrorStream())) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getJavaExePath(String JavaHome) {
        if (Pattern.compile("\\s").matcher(JavaHome).find()) {
            if (JavaHome.endsWith("\\")) {
                return "\"" + JavaHome + "bin\\java.exe\"";
            } else {
                return "\"" + JavaHome + "\\bin\\java.exe\"";
            }
        } else {
            if (JavaHome.endsWith("\\")) {
                return JavaHome + "bin\\java.exe";
            } else {
                return JavaHome + "\\bin\\java.exe";
            }
        }
    }

    public static void InspectJavaPath() throws IOException {
        SettingManger.Setting setting = SettingManger.getSetting();
        JsonArray JavaList = setting.getJavaPath();
        for (int i = 0; i < JavaList.size(); i++) {
            String JavaPath = JavaList.get(i).getAsString();
            if (PlatformUtils.FileExistenceAndSize(JavaPath)) {
                logmaker.info(JavaPath + " 无效");
                JavaList.remove(i);
            } else {
                logmaker.info(JavaPath + ",Version:" + getJavaVersion(JavaPath));
            }
        }
        setting.setJavaPath(JavaList);
        SettingManger.putSettingToFile(setting);
    }
}

