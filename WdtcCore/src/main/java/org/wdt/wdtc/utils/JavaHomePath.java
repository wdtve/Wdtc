package org.wdt.wdtc.utils;

import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.wdt.wdtc.platform.AboutSetting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaHomePath {
    private static final Logger logmaker = getWdtcLogger.getLogger(JavaHomePath.class);

    public static String GetRunJavaHome() {
        return FilenameUtils.separatorsToUnix(System.getProperty("java.home") + "\\bin\\java.exe");
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
        Process process = new ProcessBuilder(new String[]{"reg", "query", key}).start();
        for (String s : IOUtils.readLines(process.getInputStream())) {
            if (s.startsWith(key)) {
                for (Map<String, String> map : getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
                    String JavaPath = map.get("JavaPath");
                    JSONArray JavaList = AboutSetting.JavaList().getFastJSONArray();
                    boolean AddPath = true;
                    for (int i = 0; i < JavaList.size(); i++) {
                        if (AddPath) {
                            if (JavaPath.equals(JavaList.getString(i))) {
                                AddPath = false;
                            }
                        }
                    }
                    if (AddPath) {
                        JavaList.add(JavaPath);
                        logmaker.info("* Find Java : " + map.get("JavaPath") + ", Version : " + map.get("JavaVersion"));
                        PlatformUtils.PutKeyToFile(AboutSetting.GetSettingFile(), AboutSetting.SettingObject().getFastJSONObject(), "JavaPath", JavaList);
                    }
                }
            }
        }
    }


    private static List<String> getPotentialJavaFolders(String key) throws IOException {
        List<String> List = new ArrayList<>();
        Process process = new ProcessBuilder(new String[]{"reg", "query", key}).start();
        for (String s : IOUtils.readLines(process.getInputStream())) {
            if (s.startsWith(key)) {
                List.add(s);
            }
        }
        return List;
    }

    private static List<String> getPotentialJavaHome(List<String> list) throws IOException {
        List<String> JavaHomeList = new ArrayList<>();
        for (String key : list) {
            Process process = new ProcessBuilder(new String[]{"reg", "query", key, "/v", "JavaHome"}).start();
            for (String s : IOUtils.readLines(process.getInputStream())) {
                String JavaHomeCleaned = s.replaceAll("\\s", "");
                if (JavaHomeCleaned.startsWith("JavaHome")) {
                    JavaHomeList.add(JavaHomeCleaned.substring(s.indexOf("REG_SZ") - 2));
                }
            }
        }
        return JavaHomeList;
    }

    public static List<Map<String, String>> getJavaExeAndVersion(List<String> list) throws IOException {
        List<Map<String, String>> JavaList = new ArrayList<>();
        for (String path : list) {
            String JavaPath = path + "bin\\java.exe";
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


}
