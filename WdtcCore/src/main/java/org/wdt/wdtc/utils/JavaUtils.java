package org.wdt.wdtc.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.log4j.Logger;
import org.wdt.utils.io.FileUtils;
import org.wdt.utils.io.IOUtils;
import org.wdt.wdtc.manger.SettingManger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(JavaUtils.class);

    public static String getRunJavaHome() {
        return getJavaExePath(new File(System.getProperty("java.home")));
    }

    public static void main(String[] args) {
        try {
            for (String s : args) {
                getPotentialJava(s);
            }
            logmaker.info("Find Java Done");
        } catch (IOException e) {
            logmaker.error("Error, ", e);
        }
    }

    private static void getPotentialJava(String key) throws IOException {
        Process process = new ProcessBuilder("reg", "query", key).start();
        SettingManger.Setting setting = SettingManger.getSetting();
        List<JavaInfo> NewJavaList = new ArrayList<>();
        for (String s : IOUtils.readLines(process.getInputStream())) {
            if (s.startsWith(key)) {
                for (Map<String, String> map : getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
                    JavaInfo NewInfo = new JavaUtils.JavaInfo(new File(map.get("JavaPath")));
                    boolean AddPath = true;
                    for (JavaInfo info : NewJavaList) {
                        if (AddPath) {
                            if (NewInfo.equals(info)) {
                                AddPath = false;
                            }
                        }
                    }
                    if (AddPath) {
                        NewJavaList.add(NewInfo);
                        logmaker.info("Find Java : " + NewInfo.getJavaExeFile() + ", Version : " + NewInfo.getVersionNumber());
                    }
                }
            }
        }
        setting.setJavaPath(NewJavaList);
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
            String JavaPath = getJavaExePath(new File(path));
            if (!Files.exists(Paths.get(path))) {
                logmaker.warn("warn : ", new IOException(path + " isn't exists"));
            } else {
                if (!PlatformUtils.FileExistenceAndSize(JavaPath)) {
                    Map<String, String> JavaExeAndVersion = new HashMap<>();
                    JavaExeAndVersion.put("JavaPath", path);
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

    public static String getJavaExePath(File JavaHome) {
        String JavaHomePath = FileUtils.getCanonicalPath(JavaHome);
        if (Pattern.compile("\\s").matcher(JavaHomePath).find()) {
            if (JavaHomePath.endsWith("\\")) {
                return "\"" + JavaHome + "bin\\java.exe\"";
            } else {
                return "\"" + JavaHome + "\\bin\\java.exe\"";
            }
        } else {
            if (JavaHomePath.endsWith("\\")) {
                return JavaHome + "bin\\java.exe";
            } else {
                return JavaHome + "\\bin\\java.exe";
            }
        }
    }

    public static File getJavaExeFile(File JavaHome) {
        return new File(JavaHome, "bin/java.exe");
    }


    public enum JavaTips {
        JDK, JRE;

        public static boolean isJRE(File JavaHome) {
            try {
                return PlatformUtils.FileExistenceAndSize(new File(JavaHome, "bin/javac.exe"));
            } catch (IOException e) {
                logmaker.error(e);
            }
            return false;
        }

        public static JavaTips getJavaTips(File JavaHomeFile) {
            if (isJRE(JavaHomeFile)) return JRE;
            return JDK;
        }
    }

    @Setter
    @Getter
    @ToString
    public static class JavaInfo {

        private File JavaHomeFile;
        private File JavaExeFile;
        private String VersionNumber;
        private JavaTips tips;

        public JavaInfo(File javaHomeFile, File javaExeFile, String versionNumber, JavaTips tips) {
            JavaHomeFile = javaHomeFile;
            JavaExeFile = javaExeFile;
            VersionNumber = versionNumber;
            this.tips = tips;
        }

        public JavaInfo(File javaHomeFile) {
            this(javaHomeFile, JavaUtils.getJavaExeFile(javaHomeFile), getJavaVersion(getJavaExePath(javaHomeFile)), JavaTips.getJavaTips(javaHomeFile));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JavaInfo javaInfo = (JavaInfo) o;
            return Objects.equals(JavaExeFile, javaInfo.JavaExeFile) && tips == javaInfo.tips;
        }
    }
}

